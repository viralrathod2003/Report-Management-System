<template>
  <v-container fluid class="login-container">
    <v-card class="login-card">
      <v-card-title class="login-card-title">
        Welcome Back!
      </v-card-title>
      <v-card-text>
        <v-form @submit.prevent="submitLogin">
          <input v-model="username" label="Username" required placeholder="username" />
          <input v-model="password" label="Password" type="password" required placeholder="username" />
          {{ message }}
          <v-btn type="submit" color="primary" class="login-button">Login</v-btn>
        </v-form>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script>
import { useAuthStore } from '@/stores/auth.js';
import router from '@/router/index.js';
export default {
  data() {
    return {
      username: '',
      password: '',
      message: '',
    };
  },
  methods: {
    submitLogin() {
      useAuthStore().login(this.username, this.password)
        .then(loginSuccessful => {
          if (loginSuccessful) {
              if (useAuthStore().userRoles.includes('ROLE_ADMIN'))
                  router.push('/admin');

              else if (useAuthStore().userRoles.includes('ROLE_APPROVER'))
                  router.push('/approver');

              else if (useAuthStore().userRoles.includes('ROLE_USER')) 
                  router.push('/employee');
                
              else
                  console.log("unauthorized user");
          }
          else 
              this.message = "Invalid Credentials"
        });
    }
  }

};
</script>

<style scoped>
.primary {
  background-color: #1976d2;
  color: white;
  padding: 12px;
  text-align: center;
}

input {
  margin-left: auto;
  width: 100%;
  height: 50px;
  margin: 5px;
  padding: 5px;
  border-radius: 15px;
  text-align: center;
  background-color: azure;
}


.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

.login-card {
  width: 500px;
  background-color: #f2f2f2;
  border-radius: 10px;
  padding: 20px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
}

.login-card-title {
  text-align: center;
  font-size: 24px;
  margin-bottom: 20px;
}

.login-button {
  display: block;
  margin: 0px auto 0px auto;
  width: 100px;
  border-radius: 10px;
}
</style>
