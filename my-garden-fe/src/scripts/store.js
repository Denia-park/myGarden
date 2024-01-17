import {createStore} from 'vuex'

export const store = createStore({
    state() {
        return {
            account: {
                accessToken: '',
                refreshToken: '',
                roles: []
            }
        }
    },
    mutations: {
        setToken(state, payload) {
            state.account.accessToken = payload.accessToken;
            state.account.refreshToken = payload.refreshToken;
        },
        clearToken(state) {
            state.account.accessToken = '';
            state.account.refreshToken = '';
        },
        setRoles(state, payload) {
            state.account.roles = Array.isArray(payload) ? payload : [];
        },
    },
    actions: {
        initializeTokenFromSessionStorage({commit}) {
            const token = JSON.parse(sessionStorage.getItem('token'));

            if (!token) return;
            commit('setToken', token);
        }
    },
    getters: {
        getAccessToken(state) {
            return state.account.accessToken;
        },
        getRefreshToken(state) {
            return state.account.refreshToken;
        }
    }
});
