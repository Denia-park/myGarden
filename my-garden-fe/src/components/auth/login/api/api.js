import axios from "axios";
import {store} from "@/scripts/store.js";

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
