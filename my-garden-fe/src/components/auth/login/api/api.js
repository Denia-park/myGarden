import axios from "axios";
import {store} from "@/scripts/store.js";

/**
 * Login API
 *
 * @param email 이메일
 * @param password 비밀번호
 * @returns {Promise<string | null>} 성공 시 'success', 실패 시 null
 */
export function loginApi(email, password) {
    return axios.post('/api/auth/login', {
        email: email,
        password: password
    })
        .then(res => {
            const data = res.data.data;

            store.commit('setToken', data);
            sessionStorage.setItem('token', JSON.stringify(data));

            return 'success';
        })
        .catch(error => {
            return null;
        });
}
