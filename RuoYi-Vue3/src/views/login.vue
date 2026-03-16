<template>
  <div class="login-container">
    <el-form
      ref="loginRef"
      :model="loginForm"
      :rules="loginRules"
      class="login-form"
    >
      <!-- <h3 class="title">{{ title }}</h3> -->

      <div class="form">
        <p id="heading">{{ title }}</p>

        <!-- 账号 -->
        <el-form-item prop="username" class="custom-form-item">
          <div class="field">
            <svg
              class="input-icon"
              xmlns="http://www.w3.org/2000/svg"
              width="16"
              height="16"
              fill="currentColor"
              viewBox="0 0 16 16"
            >
              <path
                d="M13.106 7.222c0-2.967-2.249-5.032-5.482-5.032-3.35 0-5.646 2.318-5.646 5.702 0 3.493 2.235 5.708 5.762 5.708.862 0 1.689-.123 2.304-.335v-.862c-.43.199-1.354.328-2.29.328-2.926 0-4.813-1.88-4.813-4.798 0-2.844 1.921-4.881 4.594-4.881 2.735 0 4.608 1.688 4.608 4.156 0 1.682-.554 2.769-1.416 2.769-.492 0-.772-.28-.772-.76V5.206H8.923v.834h-.11c-.266-.595-.881-.964-1.6-.964-1.4 0-2.378 1.162-2.378 2.823 0 1.737.957 2.906 2.379 2.906.8 0 1.415-.39 1.709-1.087h.11c.081.67.703 1.148 1.503 1.148 1.572 0 2.57-1.415 2.57-3.643zm-7.177.704c0-1.197.54-1.907 1.456-1.907.93 0 1.524.738 1.524 1.907S8.308 9.84 7.371 9.84c-.895 0-1.442-.725-1.442-1.914z"
              />
            </svg>
            <input
              v-model="loginForm.username"
              autocomplete="off"
              placeholder="账号"
              class="input-field"
              type="text"
            />
          </div>
        </el-form-item>

        <!-- 密码 -->
        <el-form-item prop="password" class="custom-form-item">
          <div class="field">
            <svg
              class="input-icon"
              xmlns="http://www.w3.org/2000/svg"
              width="16"
              height="16"
              fill="currentColor"
              viewBox="0 0 16 16"
            >
              <path
                d="M8 1a2 2 0 0 1 2 2v4H6V3a2 2 0 0 1 2-2zm3 6V3a3 3 0 0 0-6 0v4a2 2 0 0 0-2 2v5a2 2 0 0 0 2 2h6a2 2 0 0 0 2-2V9a2 2 0 0 0-2-2z"
              />
            </svg>
            <input
              v-model="loginForm.password"
              placeholder="密码"
              class="input-field"
              type="password"
              autocomplete="off"
              @keyup.enter="handleLogin"
            />
          </div>
        </el-form-item>

        <!-- 验证码 -->
        <el-form-item
          prop="code"
          v-if="captchaEnabled"
          class="custom-form-item captcha-form-item"
        >
          <div class="captcha-row">
            <div class="field captcha-field">
              <svg-icon icon-class="validCode" class="input-icon captcha-icon" />
              <input
                v-model="loginForm.code"
                placeholder="验证码"
                class="input-field"
                type="text"
                autocomplete="off"
                @keyup.enter="handleLogin"
              />
            </div>
            <div class="login-code">
              <img
                :src="codeUrl"
                @click="getCode"
                class="login-code-img"
                alt="验证码"
              />
            </div>
          </div>
        </el-form-item>

        <!-- 记住密码 -->
        <div class="options-row">
          <el-checkbox v-model="loginForm.rememberMe">记住密码</el-checkbox>
          <router-link v-if="register" class="link-type" to="/register">
            立即注册
          </router-link>
        </div>

        <!-- 按钮 -->
        <div class="btn">
          <button
            type="button"
            class="button1"
            :disabled="loading"
            @click="handleLogin"
          >
            <span v-if="!loading">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;登录&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
            <span v-else>&nbsp;&nbsp;登录中...&nbsp;&nbsp;</span>
          </button>

          <button
            v-if="register"
            type="button"
            class="button2"
            @click="$router.push('/register')"
          >
            注册
          </button>
        </div>
      </div>
    </el-form>
    <!--  底部  -->
    <div class="el-login-footer">
      <span>{{ footerContent }}</span>
    </div>
  </div>
</template>

<script setup>
import { getCodeImg } from "@/api/login"
import Cookies from "js-cookie"
import { encrypt, decrypt } from "@/utils/jsencrypt"
import useUserStore from '@/store/modules/user'
import defaultSettings from '@/settings'

const title = import.meta.env.VITE_APP_TITLE
const footerContent = defaultSettings.footerContent
const userStore = useUserStore()
const route = useRoute()
const router = useRouter()
const { proxy } = getCurrentInstance()

const loginForm = ref({
  username: "admin",
  password: "admin123",
  rememberMe: false,
  code: "",
  uuid: ""
})

const loginRules = {
  username: [{ required: true, trigger: "blur", message: "请输入您的账号" }],
  password: [{ required: true, trigger: "blur", message: "请输入您的密码" }],
  code: [{ required: true, trigger: "change", message: "请输入验证码" }]
}

const codeUrl = ref("")
const loading = ref(false)
// 验证码开关
const captchaEnabled = ref(true)
// 注册开关
const register = ref(true)
const redirect = ref(undefined)

watch(route, (newRoute) => {
    redirect.value = newRoute.query && newRoute.query.redirect
}, { immediate: true })

function handleLogin() {
  proxy.$refs.loginRef.validate(valid => {
    if (valid) {
      loading.value = true
      // 勾选了需要记住密码设置在 cookie 中设置记住用户名和密码
      if (loginForm.value.rememberMe) {
        Cookies.set("username", loginForm.value.username, { expires: 30 })
        Cookies.set("password", encrypt(loginForm.value.password), { expires: 30 })
        Cookies.set("rememberMe", loginForm.value.rememberMe, { expires: 30 })
      } else {
        // 否则移除
        Cookies.remove("username")
        Cookies.remove("password")
        Cookies.remove("rememberMe")
      }
      // 调用action的登录方法
      userStore.login(loginForm.value).then(() => {
        const query = route.query
        const otherQueryParams = Object.keys(query).reduce((acc, cur) => {
          if (cur !== "redirect") {
            acc[cur] = query[cur]
          }
          return acc
        }, {})
        router.push({ path: redirect.value || "/", query: otherQueryParams })
      }).catch(() => {
        loading.value = false
        // 重新获取验证码
        if (captchaEnabled.value) {
          getCode()
        }
      })
    }
  })
}

function getCode() {
  getCodeImg().then(res => {
    captchaEnabled.value = res.captchaEnabled === undefined ? true : res.captchaEnabled
    if (captchaEnabled.value) {
      codeUrl.value = "data:image/gif;base64," + res.img
      loginForm.value.uuid = res.uuid
    }
  })
}

function getCookie() {
  const username = Cookies.get("username")
  const password = Cookies.get("password")
  const rememberMe = Cookies.get("rememberMe")
  loginForm.value = {
    username: username === undefined ? loginForm.value.username : username,
    password: password === undefined ? loginForm.value.password : decrypt(password),
    rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
  }
}

getCode()
getCookie()
</script>

<style lang='scss' scoped>
// .login {
//   display: flex;
//   justify-content: center;
//   align-items: center;
//   height: 100%;
//   background-image: url("../assets/images/login-background.jpg");
//   background-size: cover;
// }
body {
  margin: 0;
  min-height: 100vh;
  background: linear-gradient(135deg, #74ebd5, #9face6);
}
.login-container {
  width: 100%;
  min-height: 100vh;
  background: linear-gradient(135deg, #e0eafc, #cfdef3);
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-form {
  width: 420px;
}

.title {
  text-align: center;
  color: #333;
  margin-bottom: 20px;
  font-size: 26px;
  font-weight: 700;
}

.form {
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding: 2em;
  background-color: #171717;
  border-radius: 25px;
  transition: 0.4s ease-in-out;
}

.form:hover {
  transform: scale(1.02);
  border: 1px solid #fff;
}

#heading {
  text-align: center;
  margin: 0 0 8px;
  color: rgb(255, 255, 255);
  font-size: 1.5em;
  font-weight: 700;
}

.custom-form-item {
  margin-bottom: 0;
}

:deep(.custom-form-item .el-form-item__content) {
  display: block;
}

:deep(.custom-form-item .el-form-item__error) {
  position: static;
  padding-top: 6px;
  color: #ff6b6b;
}

.field {
  display: flex;
  align-items: center;
  gap: 0.8em;
  border-radius: 25px;
  padding: 0.85em 1em;
  border: none;
  outline: none;
  color: white;
  background-color: #1f1f1f;
  box-shadow: inset 2px 5px 10px rgb(5, 5, 5);
}

.input-icon {
  width: 1.2em;
  height: 1.2em;
  color: white;
  flex-shrink: 0;
}

.input-field {
  background: none;
  border: none;
  outline: none;
  width: 100%;
  color: #d3d3d3;
  font-size: 14px;
}

.captcha-row {
  display: flex;
  gap: 12px;
  align-items: center;
}

.captcha-field {
  flex: 1;
}

.login-code {
  width: 120px;
  height: 44px;
  border-radius: 10px;
  overflow: hidden;
  flex-shrink: 0;
}

.login-code-img {
  width: 100%;
  height: 100%;
  display: block;
  cursor: pointer;
  object-fit: cover;
  border-radius: 10px;
}

.options-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: #fff;
  margin-top: 4px;
}

.link-type {
  color: #4c9eff;
  text-decoration: none;
  font-size: 14px;
}

.link-type:hover {
  text-decoration: underline;
}

.btn {
  display: flex;
  justify-content: center;
  gap: 14px;
  margin-top: 10px;
}

.button1,
.button2 {
  padding: 0.7em 1.4em;
  border-radius: 8px;
  border: none;
  outline: none;
  transition: 0.4s ease-in-out;
  cursor: pointer;
  font-weight: 600;
}

.button1 {
  background-color: #252525;
  color: white;
}

.button1:hover {
  background-color: black;
  color: white;
}

.button1:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.button2 {
  background-color: #fff;
  color: #111;
}

.button2:hover {
  background-color: #e5e5e5;
}

:deep(.el-checkbox) {
  color: #fff;
}

:deep(.el-checkbox__label) {
  color: #fff;
}
.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}
</style>
