import axios from "axios";
import {store} from "@/scripts/store.js";
import {router} from "@/scripts/router.js";

let isRefreshing = false;

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

    axios.interceptors.response.use(
        (res) => {
            return res;
        },
        async (err) => {
            const originalConfig = err.config;

            // Access Token이 만료됨
            if (err && err.response.status === 401 && !isRefreshing) {
                isRefreshing = true;

                try {
                    const result = await refreshTokenApi();

                    if (!result) {
                        return Promise.reject(err);
                    }

                    store.commit('setToken', result);
                    sessionStorage.setItem('token', JSON.stringify(result));

                    return axios(originalConfig);
                } catch (_error) {
                    if (_error.response?.data) {
                        return Promise.reject(_error.response.data);
                    }

                    return Promise.reject(_error);
                }
            } else if (err && err.response.status === 400 && isRefreshing) {
                isRefreshing = false;

                // Refresh Token이 만료되었거나, 다른 이유로 인해 재시도를 하지 못한 경우 (다른 브라우저에서 로그아웃, 다른 브라우저에서 로그인 등)
                //로그인 정보 삭제, 세션 스토리지 삭제
                store.commit('clearToken');
                sessionStorage.removeItem('token');

                alert("로그인이 필요합니다.")

                //로그인 페이지로 이동
                await router.push('/login');

                return Promise.reject(err.response.data);
            }

            if (err.response.status === 403) {
                isRefreshing = false;

                await router.push('/notFound');
                return Promise.reject(err.response.data);
            }

            return Promise.reject(err);
        }
    );
};

const refreshTokenApi = async () => {
    return axios.post("/api/auth/refresh", {
        refreshToken: store.getters.getRefreshToken,
    })
        .then(res => {
            return res.data.data;
        })
        .catch(error => {
            return null;
        });
};

export default setup;
