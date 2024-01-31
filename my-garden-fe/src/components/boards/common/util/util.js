import {store} from "@/scripts/store.js";

export function convertCategoryCodeToText(categories, categoryCode) {
    const category = categories.find(category => category.code === categoryCode);
    return category?.text;
}

export function isUserAccount() {
    const roles = store.getters.getRoles;

    return roles.includes("ROLE_USER")
        || roles.includes("ROLE_ADMIN");
}

export function isAdminAccount() {
    const roles = store.getters.getRoles;

    return roles.includes("ROLE_ADMIN");
}
