import axios from "axios";
import {store} from "@/scripts/store.js";
import {router} from "@/scripts/router.js";

/**
 * Access Token이 만료되었을 때, Refresh Token을 사용하여 새로운 Access Token을 발급받는 중인지 여부
 */
let isRefreshing = 0;

/**
 * Axios Interceptor Request 설정 <br>
 * - Request Interceptor: 토큰이 필요한 API에 접근시 Header에 적절한 토큰을 추가
 */
const setup = () => {
    axios.interceptors.request.use(
        (config) => {
            /**
             * 토큰이 필요 없는 URL 목록
             */
            const WHITE_LIST = ["/api/auth/signup", "/api/auth/login"];
            /**
             * Refresh Token을 사용하는 URL 목록
             */
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

    /**
     * Axios Interceptor Response 설정 <br>
     * - Response Interceptor: 토큰이 만료되었을 때, Refresh Token을 사용하여 새로운 Access Token을 발급받는 중인지 여부를 확인하고,
     * 새로운 Access Token을 발급받은 후, 다시 API 요청을 보내는 로직
     */
    axios.interceptors.response.use(
        (res) => {
            isRefreshing = 0;
            return res;
        },
        async (err) => {
            const originalConfig = err.config;

            // Access Token이 만료됨
            if (err && err.response.status === 401 && isRefreshing < 3) {
                isRefreshing++;

                try {
                    // Refresh Token을 사용하여 새로운 Access Token을 발급받는 요청
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
            } else if (err && err.response.status === 400 && isRefreshing > 3) {
                isRefreshing = 0;

                // Refresh Token이 만료되었거나, 다른 이유로 인해 재시도를 하지 못한 경우 (다른 브라우저에서 로그아웃, 다른 브라우저에서 로그인 등)
                //로그인 정보 삭제, 세션 스토리지 삭제
                store.commit('clearToken');
                sessionStorage.removeItem('token');

                alert("로그인이 필요합니다.")

                //로그인 페이지로 이동
                await router.push('/login');

                return Promise.reject(err.response.data);
            }

            // 응답 코드가 403인 경우 (권한 없음) -> 403 페이지로 이동
            if (err.response.status === 403) {
                isRefreshing = false;

                await router.push('/notFound');
                return Promise.reject(err.response.data);
            }

            return Promise.reject(err);
        }
    );
};

/**
 * Refresh Token을 사용하여 새로운 Access Token을 발급받는 요청
 */
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
