import { ref, computed } from 'vue';
import { defineStore } from 'pinia';
import axios from '@/plugins/axios';

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token:'',
    username:'',
    userRoles: []
  }),

  getters: {
    isAuthenticated: state => computed(() => !!localStorage.getItem("token")),
    hasRole: state => role => computed(() => state.localStorage.getItem("roles").includes(role.value))
  },
  actions: {
    setToken(token) {
      this.token = token;
    },
    setRoles(userRoles) {
      this.userRoles = userRoles;
    },
    setUsername(username) {
      this.username = username;
    },

    logout() {
      this.token = '', this.userRoles = [], this.username = ''
      localStorage.removeItem("token");
      localStorage.removeItem("username");
      localStorage.removeItem("roles");
    },


    async login(username, password) {
      const credentials = btoa(`${username}:${password}`);
      const headers = { Authorization: `Basic ${credentials}` };
      localStorage.removeItem("token")
      return await axios
        .get('http://localhost:8080/authenticate', { headers })
        .then(response => {
          const { token, roles } = response.data;
          this.setToken(token);
          this.setRoles(roles);
          this.setUsername(username);

          localStorage.setItem("token", token);
          localStorage.setItem("username", username);
          localStorage.setItem("roles",roles);
          
          return true;
        })
        .catch(error => {
          console.error(error);
          return false;
        });
    },
  }
});
