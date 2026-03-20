package com.ruoyi.Xidian.support;

import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
public class PathLockManager
{
    private final ConcurrentMap<String, ReentrantReadWriteLock> lockStore = new ConcurrentHashMap<>();

    public LockHandle lockRead(Path... readPaths)
    {
        List<Path> paths = new ArrayList<>();
        if (readPaths != null)
        {
            Collections.addAll(paths, readPaths);
        }
        return lock(paths, Collections.<Path>emptyList());
    }

    public LockHandle lockWrite(Path... writePaths)
    {
        List<Path> paths = new ArrayList<>();
        if (writePaths != null)
        {
            Collections.addAll(paths, writePaths);
        }
        return lock(Collections.<Path>emptyList(), paths);
    }

    public LockHandle lock(Collection<Path> readPaths, Collection<Path> writePaths)
    {
        Map<String, Boolean> lockModes = new TreeMap<>();
        collectLockModes(lockModes, readPaths, false);
        collectLockModes(lockModes, writePaths, true);

        List<Lock> acquiredLocks = new ArrayList<>();
        try
        {
            for (Map.Entry<String, Boolean> entry : lockModes.entrySet())
            {
                ReentrantReadWriteLock readWriteLock =
                        lockStore.computeIfAbsent(entry.getKey(), key -> new ReentrantReadWriteLock(true));
                Lock lock = entry.getValue() ? readWriteLock.writeLock() : readWriteLock.readLock();
                lock.lock();
                acquiredLocks.add(lock);
            }
            return new LockHandle(acquiredLocks);
        }
        catch (RuntimeException ex)
        {
            unlockAll(acquiredLocks);
            throw ex;
        }
    }

    private void collectLockModes(Map<String, Boolean> lockModes, Collection<Path> paths, boolean write)
    {
        if (paths == null)
        {
            return;
        }

        for (Path path : paths)
        {
            if (path == null)
            {
                continue;
            }
            String key = normalize(path);
            Boolean currentMode = lockModes.get(key);
            if (currentMode == null || write)
            {
                lockModes.put(key, write);
            }
        }
    }

    private String normalize(Path path)
    {
        return path.toAbsolutePath().normalize().toString();
    }

    private void unlockAll(List<Lock> acquiredLocks)
    {
        for (int i = acquiredLocks.size() - 1; i >= 0; i--)
        {
            acquiredLocks.get(i).unlock();
        }
    }

    public static final class LockHandle implements AutoCloseable
    {
        private final List<Lock> acquiredLocks;

        private LockHandle(List<Lock> acquiredLocks)
        {
            this.acquiredLocks = acquiredLocks;
        }

        @Override
        public void close()
        {
            for (int i = acquiredLocks.size() - 1; i >= 0; i--)
            {
                acquiredLocks.get(i).unlock();
            }
        }
    }
}
