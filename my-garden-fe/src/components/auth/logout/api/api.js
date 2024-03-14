import axios from "axios";
import {store} from "@/scripts/store.js";

/**
 * Logout API
 *
 * @param email 이메일
 * @param password 비밀번호
 * @returns {Promise<string | null>} 성공 시 'success', 실패 시 null
 */
export function logoutApi(email, password) {
    return axios.post('/api/auth/logout',)
        .then(res => {
            store.commit('clearToken');
            sessionStorage.removeItem('token');

            return 'success';
        })
        .catch(error => {
            return null;
        });
}
