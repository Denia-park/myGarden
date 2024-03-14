import {store} from "@/scripts/store.js";

/**
 * 분류 목록에서 분류 코드에 해당하는 분류명을 반환
 *
 * @param categories 분류 목록
 * @param categoryCode 분류 코드
 * @returns {string} 분류명
 */
export function convertCategoryCodeToText(categories, categoryCode) {
    const category = categories.find(category => category.code === categoryCode);
    return category?.text;
}

/**
 * 사용자 계정인지 확인
 *
 * @returns {boolean} 사용자 계정이면 true, 아니면 false
 */
export function isUserAccount() {
    const roles = store.getters.getRoles;

    return roles.includes("ROLE_USER")
        || roles.includes("ROLE_ADMIN");
}

/**
 * 관리자 계정인지 확인
 *
 * @returns {boolean} 관리자 계정이면 true, 아니면 false
 */
export function isAdminAccount() {
    const roles = store.getters.getRoles;

    return roles.includes("ROLE_ADMIN");
}
