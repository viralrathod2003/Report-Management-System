import axios from "axios";
import { useAuthStore } from "../stores/auth";
import config from "../config";
import { useRouter } from "vue-router";

const axiosInstance = axios.create({
  baseURL: config.apiBaseUrl,
});

axiosInstance.interceptors.request.use(
  (config) => {
    if (localStorage.getItem("token")) {
      config.headers = {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
        "Content-type": "application/json;charset=UTF-8",
      };
    }

    return config;
  },
  (error) => Promise.reject(error)
);

axiosInstance.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    const authStore = useAuthStore();
    const router = useRouter();
    if (
      error.response.status === 401 &&
      error.response.data.error === "Unauthorized"
    ) {
      authStore.logout();
      router.replace({ name: "login" });
    }
    return Promise.reject(error);
  }
);

export default axiosInstance;