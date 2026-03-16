<template>
  <div class="register">
    <el-form ref="registerRef" :model="registerForm" :rules="registerRules" class="register-form" autocomplete="off">
      <div class="form">
        <p id="heading">{{ title }}</p>
        <input class="register-autofill-guard" type="text" name="fake_username" autocomplete="username" tabindex="-1" />
        <input class="register-autofill-guard" type="password" name="fake_password" autocomplete="current-password" tabindex="-1" />

        <el-form-item prop="username" class="custom-form-item">
          <el-input
            v-model="registerForm.username"
            type="text"
            size="large"
            name="register_username"
            autocomplete="off"
            placeholder="账号"
          >
            <template #prefix><svg-icon icon-class="user" class="el-input__icon input-icon" /></template>
          </el-input>
        </el-form-item>

        <el-form-item prop="password" class="custom-form-item">
          <el-input
            v-model="registerForm.password"
            type="password"
            size="large"
            name="register_password"
            autocomplete="new-password"
            placeholder="密码"
            @keyup.enter="handleRegister"
          >
            <template #prefix><svg-icon icon-class="password" class="el-input__icon input-icon" /></template>
          </el-input>
        </el-form-item>

        <el-form-item prop="confirmPassword" class="custom-form-item">
          <el-input
            v-model="registerForm.confirmPassword"
            type="password"
            size="large"
            name="register_confirm_password"
            autocomplete="new-password"
            placeholder="确认密码"
            @keyup.enter="handleRegister"
          >
            <template #prefix><svg-icon icon-class="password" class="el-input__icon input-icon" /></template>
          </el-input>
        </el-form-item>

        <el-form-item prop="code" v-if="captchaEnabled" class="custom-form-item captcha-form-item">
          <el-input
            size="large"
            v-model="registerForm.code"
            name="register_code"
            autocomplete="off"
            placeholder="验证码"
            @keyup.enter="handleRegister"
          >
            <template #prefix><svg-icon icon-class="validCode" class="el-input__icon input-icon" /></template>
          </el-input>
          <div class="register-code">
            <img :src="codeUrl" @click="getCode" class="register-code-img" alt="验证码" />
          </div>
        </el-form-item>

        <el-form-item class="custom-form-item register-action-item">
          <el-button
            :loading="loading"
            size="large"
            type="primary"
            @click.prevent="handleRegister"
          >
            <span v-if="!loading">注册</span>
            <span v-else>注册中...</span>
          </el-button>
          <div class="register-extra">
            <router-link class="link-type" :to="'/login'">使用已有账户登录</router-link>
          </div>
        </el-form-item>
      </div>
    </el-form>

    <div class="el-register-footer">
      <span>{{ footerContent }}</span>
    </div>
  </div>
</template>

<script setup>
import { ElMessageBox } from "element-plus"
import { getCodeImg, register } from "@/api/login"
import defaultSettings from "@/settings"

const title = import.meta.env.VITE_APP_TITLE
const footerContent = defaultSettings.footerContent
const router = useRouter()
const { proxy } = getCurrentInstance()

const registerForm = ref({
  username: "",
  password: "",
  confirmPassword: "",
  code: "",
  uuid: ""
})

const equalToPassword = (rule, value, callback) => {
  if (registerForm.value.password !== value) {
    callback(new Error("两次输入的密码不一致"))
  } else {
    callback()
  }
}

const registerRules = {
  username: [
    { required: true, trigger: "blur", message: "请输入您的账号" },
    { min: 2, max: 20, message: "用户账号长度必须介于 2 和 20 之间", trigger: "blur" }
  ],
  password: [
    { required: true, trigger: "blur", message: "请输入您的密码" },
    { min: 5, max: 20, message: "用户密码长度必须介于 5 和 20 之间", trigger: "blur" },
    { pattern: /^[^<>"'|\\]+$/, message: "不能包含非法字符：< > \" ' \\ |", trigger: "blur" }
  ],
  confirmPassword: [
    { required: true, trigger: "blur", message: "请再次输入您的密码" },
    { required: true, validator: equalToPassword, trigger: "blur" }
  ],
  code: [{ required: true, trigger: "change", message: "请输入验证码" }]
}

const codeUrl = ref("")
const loading = ref(false)
const captchaEnabled = ref(true)

function handleRegister() {
  proxy.$refs.registerRef.validate(valid => {
    if (valid) {
      loading.value = true
      register(registerForm.value).then(res => {
        const username = registerForm.value.username
        ElMessageBox.alert(`<font color='red'>恭喜你，您的账号 ${username} 注册成功！</font>`, "系统提示", {
          dangerouslyUseHTMLString: true,
          type: "success",
        }).then(() => {
          router.push("/login")
        }).catch(() => {})
      }).catch(() => {
        loading.value = false
        if (captchaEnabled) {
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
      registerForm.value.uuid = res.uuid
    }
  })
}

getCode()
</script>

<style lang="scss" scoped>
.register {
  width: 100%;
  min-height: 100vh;
  background: linear-gradient(135deg, #e0eafc, #cfdef3);
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 32px 16px 72px;
}

.register-form {
  width: 420px;
  max-width: 100%;
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
  color: #fff;
  font-size: 1.5em;
  font-weight: 700;
}

.register-autofill-guard {
  position: absolute;
  width: 1px;
  height: 1px;
  padding: 0;
  margin: 0;
  border: 0;
  opacity: 0;
  pointer-events: none;
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

:deep(.register-form .el-input) {
  --el-input-hover-border: transparent;
}

:deep(.register-form .el-input__wrapper) {
  min-height: 48px;
  padding: 0 16px;
  border-radius: 25px;
  background-color: #1f1f1f;
  box-shadow: inset 2px 5px 10px rgb(5, 5, 5) !important;
}

:deep(.register-form .el-input__wrapper.is-focus) {
  box-shadow: inset 2px 5px 10px rgb(5, 5, 5), 0 0 0 1px #4c9eff !important;
}

:deep(.register-form .el-input__inner) {
  height: 48px;
  color: #d3d3d3;
  font-size: 14px;
}

:deep(.register-form .el-input__inner::placeholder) {
  color: #8f96a3;
}

.input-icon {
  width: 1.2em;
  height: 1.2em;
  color: #fff;
}

:deep(.captcha-form-item .el-form-item__content) {
  display: flex;
  align-items: flex-start;
  gap: 12px;
}

:deep(.captcha-form-item .el-input) {
  flex: 1;
}

.register-code {
  width: 120px;
  height: 48px;
  border-radius: 10px;
  overflow: hidden;
  flex-shrink: 0;
}

.register-code-img {
  width: 100%;
  height: 100%;
  display: block;
  cursor: pointer;
  object-fit: cover;
}

.register-action-item {
  margin-top: 10px;
}

:deep(.register-action-item .el-form-item__content) {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

:deep(.register-action-item .el-button) {
  width: 100%;
  height: 44px;
  margin-left: 0;
  border: none;
  border-radius: 8px;
  background: #252525;
  color: #fff;
  font-weight: 600;
  transition: 0.4s ease-in-out;
}

:deep(.register-action-item .el-button:hover) {
  background: #000;
  color: #fff;
}

:deep(.register-action-item .el-button.is-loading) {
  opacity: 0.85;
}

.register-extra {
  display: flex;
  justify-content: flex-end;
}

.link-type {
  color: #4c9eff;
  text-decoration: none;
  font-size: 14px;
}

.link-type:hover {
  text-decoration: underline;
}

.el-register-footer {
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

@media (max-width: 520px) {
  .register {
    padding: 24px 12px 72px;
  }

  .form {
    padding: 1.5em;
    border-radius: 20px;
  }

  :deep(.captcha-form-item .el-form-item__content) {
    flex-direction: column;
  }

  .register-code {
    width: 100%;
  }
}
</style>
