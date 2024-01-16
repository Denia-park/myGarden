import axios from "axios";
import {store} from "@/scripts/store.js";

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
