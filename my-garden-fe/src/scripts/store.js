import {createStore} from 'vuex'

export const store = createStore({
    state() {
        return {
            account: {
                accessToken: '',
                refreshToken: ''
            }
        }
    },
    mutations: {
        setAccessToken(state, payload) {
            state.account.accessToken = payload;
        },
        setRefreshToken(state, payload) {
            state.account.refreshToken = payload;
        },
    },
})
