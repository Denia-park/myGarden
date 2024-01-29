export function convertCategoryCodeToText(categories, categoryCode) {
    const category = categories.find(category => category.code === categoryCode);
    return category?.text;
}
