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

            alert("로그인에 성공했습니다.");

            return 'success';
        })
        .catch(error => {
            alert("로그인에 실패했습니다.");
            console.log(error);

            return null;
        });
}
