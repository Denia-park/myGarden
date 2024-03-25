export function emailToUrlSafeBase64(email) {
    try {
        let base64 = btoa(email); // 이메일 ASCII 인코딩 시도
        return base64.replace(/\+/g, '-').replace(/\//g, '_').replace(/=+$/, ''); // URL-safe 변환
    } catch (error) {
        console.error("Encoding failed: ", error);
        // ASCII 문자열이 아닌 경우 예외 처리
        return null; // 실패시 null 반환 또는 적절한 에러 처리
    }
}

export function urlSafeBase64ToEmail(urlSafeBase64) {
    try {
        let base64 = urlSafeBase64.replace(/-/g, '+').replace(/_/g, '/');
        while (base64.length % 4) {
            base64 += '=';
        }
        return atob(base64);
    } catch (error) {
        console.error("Decoding failed: ", error);
        // 디코딩 과정에서의 예외 처리
        return null; // 실패시 null 반환 또는 적절한 에러 처리
    }
}
