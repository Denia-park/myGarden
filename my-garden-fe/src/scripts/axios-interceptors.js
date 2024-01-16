import axios from "axios";
import {store} from "@/scripts/store.js";

const setup = () => {
    axios.interceptors.request.use(
        (config) => {
            const WHITE_LIST = ["/api/auth/signup", "/api/auth/login"];
            const USING_REFRESH_TOKEN_URL_LIST = ["/api/auth/logout"];

            if (WHITE_LIST.includes(config.url)) {
                return config;
            }

            let token = store.getters.getAccessToken;
            if (USING_REFRESH_TOKEN_URL_LIST.includes(config.url)) {
                token = store.getters.getRefreshToken;
            }

            if (token) {
                config.headers["Authorization"] = `Bearer ${token}`;
            }

            return config;
        },
        (error) => {
            return Promise.reject(error);
        }
    );

    // axios.interceptors.response.use(
    //     (res) => {
    //         return res;
    //     },
    //     async (err) => {
    //         const originalConfig = err.config;
    //
    //         if (err.response) {
    //             return Promise.reject(err);
    //         }
    //
    //         // Access Token이 만료됨
    //         if (err.response.status === 401 && !originalConfig._retry) {
    //             originalConfig._retry = true;
    //
    //             try {
    //                 const result = await refreshTokenApi();
    //
    //                 if (!result) {
    //                     return Promise.reject(err);
    //                 }
    //
    //                 store.commit('setToken', result);
    //                 sessionStorage.setItem('token', JSON.stringify(result));
    //
    //                 return axios(originalConfig);
    //             } catch (_error) {
    //                 if (_error.response?.data) {
    //                     return Promise.reject(_error.response.data);
    //                 }
    //
    //                 return Promise.reject(_error);
    //             }
    //         }
    //
    //         if (err.response.status === 403 && err.response.data) {
    //             return Promise.reject(err.response.data);
    //         }
    //
    //         return Promise.reject(err);
    //     }
    // );
};

const refreshTokenApi = async () => {
    return axios.post("/api/auth/refresh", {
        refreshToken: store.getters.getRefreshToken,
    })
        .then(res => {
            return res.data.data;
        })
        .catch(error => {
            console.log(error);
            return null;
        });
};

export default setup;
