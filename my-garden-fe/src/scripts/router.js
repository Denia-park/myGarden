import DailyRoutine from "@/pages/dailyRoutine/DailyRoutine.vue";
import Login from "@/pages/Login.vue";
import {createRouter, createWebHistory} from "vue-router";
import NotFound from "@/pages/NotFound.vue";
import SignUp from "@/pages/SignUp.vue";
import {store} from "@/scripts/store.js";
import NoticeBoardList from "@/pages/boards/notice/NoticeBoardList.vue";
import NoticeBoardView from "@/pages/boards/notice/NoticeBoardView.vue";
import NoticeBoardWrite from "@/pages/boards/notice/NoticeBoardWrite.vue";
import LearnBoardList from "@/pages/boards/learn/LearnBoardList.vue";
import LearnBoardView from "@/pages/boards/learn/LearnBoardView.vue";
import LearnBoardWrite from "@/pages/boards/learn/LearnBoardWrite.vue";
import Statistics from "@/pages/dailyRoutine/DailyRoutineStatistics.vue";
import StudyHoursView from "@/pages/dailyRoutine/StudyHoursView.vue";

/**
 * 라우터 설정
 */
const routes = [
    {path: '/login', component: Login},
    {path: '/signup', component: SignUp},
    {path: '/notFound', component: NotFound},
    {path: '/daily-routine', component: DailyRoutine, meta: {permitRoles: ['ROLE_USER', 'ROLE_ADMIN']}},
    {path: '/daily-routine/statistics', component: Statistics, meta: {permitRoles: ['ROLE_USER', 'ROLE_ADMIN']}},
    {path: '/daily-routine/study-hours-view/:urlSafeBase64MemberEmail', component: StudyHoursView},
    {path: '/boards/notice', component: NoticeBoardList, name: 'NoticeBoardList'},
    {path: '/boards/notice/:boardId', component: NoticeBoardView, name: 'NoticeBoardView'},
    {
        path: '/boards/notice/write',
        component: NoticeBoardWrite,
        name: 'NoticeBoardWrite',
        meta: {permitRoles: ['ROLE_ADMIN']}
    },
    {
        path: '/boards/notice/write/:boardId',
        component: NoticeBoardWrite,
        name: 'NoticeBoardEdit',
        meta: {permitRoles: ['ROLE_ADMIN']}
    },
    {path: '/boards/learn', component: LearnBoardList, name: 'LearnBoardList'},
    {path: '/boards/learn/:boardId', component: LearnBoardView, name: 'LearnBoardView'},
    {
        path: '/boards/learn/write',
        component: LearnBoardWrite,
        name: 'LearnBoardWrite',
        meta: {permitRoles: ['ROLE_USER', 'ROLE_ADMIN']}
    },
    {
        path: '/boards/learn/write/:boardId',
        component: LearnBoardWrite,
        name: 'LearnBoardEdit',
        meta: {permitRoles: ['ROLE_USER', 'ROLE_ADMIN']}
    },

    {path: '/', redirect: '/login'},
    {path: '/:pathMatch(.*)*', redirect: '/notFound'}
]

export const router = createRouter({
    history: createWebHistory(),
    routes
})

/**
 * 인증을 위한 권한 체크 함수
 *
 * @param permitRoles 허용된 권한 목록
 * @param authenticationState 인증 상태
 * @returns {boolean} 권한이 없는지 여부
 */
function isNotAuthorized(permitRoles, authenticationState) {
    return !isAuthorized(permitRoles, authenticationState);
}

/**
 * 인증을 위한 권한 체크 함수
 *
 * @param permitRoles 허용된 권한 목록
 * @param authenticationState 인증 상태
 * @returns {boolean} 권한이 있는지 여부
 */
function isAuthorized(permitRoles, authenticationState) {
    for (const permitRole of permitRoles) {
        if (authenticationState?.roles?.includes(permitRole)) {
            return true;
        }
    }

    return false;
}

/**
 * 라우터 이동 전에 실행되는 함수 <br>
 * - 로그인 및 권한에 맞춰서 페이지 이동을 제어한다.
 *
 * @param router 라우터
 */
const routerBeforeEach = (router) => {
    router.beforeEach((to, from, next) => {
        // Token 값을 기반으로 account의 roles를 초기화
        if (!store.getters.getRoles.length) {
            store.dispatch('initializeAuthenticationRoles', store);
        }

        // authenticationState는 유저가 로그인이 되어있는지 아닌지 값을 가져와 판별해준다.
        const authenticationState = {
            isLogin: store.getters.getAccessToken,
            roles: store.getters.getRoles,
        }
        // permitRoles에서는 라우터에서 메타 속성을 정의해준 값이 담겨진다.
        // undefined, [], ["ROLE_USER"], ["ROLE_ADMIN"]가 올 수 있다.
        const {permitRoles} = to.meta;

        if (!permitRoles) {
            // permitRoles가 없는 라우터들은 로그인 없이 그냥 통과시킨다.
            return next();
        }

        // permitRoles가 있는 라우터들은 로그인이 되어있지 않으면 로그인 페이지로 보내고
        // 권한이 없는 유저는 not-found 페이지로 보낸다.
        if (!authenticationState?.isLogin) {
            // 로그인이 되어있지 않으면 로그인 페이지로 보낸다.
            alert("로그인이 필요합니다.")
            return next({path: "/login"});
        }

        if (
            permitRoles.length &&
            isNotAuthorized(permitRoles, authenticationState)
        ) {
            // 권한이 없는 유저는 not-found 페이지로 보낸다.
            alert("권한이 없습니다.")
            return next({path: "/notFound"});
        }

        // permitRoles가 빈 객체인 라우터들은 로그인만 되어있으면 다 접근할 수 있기 때문에
        // next를 통해서 그대로 통과된다.
        next();
    })
}

export {routerBeforeEach};
