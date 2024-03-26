import axios from "axios";
import {
    isToday,
    saveLastStartDateTimeInLocalStorage,
    updateLastStartDateTime
} from "@/components/dailyRoutine/api/util.js";

/**
 * 데일리 루틴 목록 조회 API
 *
 * @param startDateTime 조회 시작 일자
 * @param endDateTime 조회 종료 일자
 * @returns {Promise<{allDateTimeDataArray: *} | void>} 성공 시 데일리 루틴 목록, 실패 시 alert
 */
export function getDailyRoutineApi(startDateTime, endDateTime) {
    return axios.get(`/api/daily-routine?startDateTime=${startDateTime}&endDateTime=${endDateTime}`)
        .then(({data}) => {
            const allDateTimeDataArray = data.data;

            //오늘 날짜인 경우에만 LocalStorage를 업데이트
            if (isToday(startDateTime)) {
                saveLastStartDateTimeInLocalStorage(startDateTime, allDateTimeDataArray);
            }

            return {
                allDateTimeDataArray
            };
        })
        .catch(error => {
            alert('일정을 불러오는데 실패했습니다.')
        });
}

/**
 * 데일리 루틴 등록 API
 *
 * @param startDate 시작 일자
 * @param endDate 종료 일자
 * @param routineType 루틴 타입
 * @param content 루틴 내용
 */
export function postDailyRoutineApi(startDate, endDate, routineType, content) {
    axios.post('/api/daily-routine', {
        startDateTime: startDate,
        endDateTime: endDate,
        routineType: routineType,
        routineDescription: content
    })
        .then(() => {
            alert("등록되었습니다.");
            location.reload();
        })
        .catch(error => {
            alert("등록에 실패했습니다.");

        });
}

/**
 * 데일리 루틴 수정 API
 *
 * @param id 수정할 데일리 루틴 ID
 * @param startDate 시작 일자
 * @param endDate 종료 일자
 * @param routineType 루틴 타입
 * @param content 루틴 내용
 */
export function updateDailyRoutineApi(id, startDate, endDate, routineType, content) {
    axios.put(`/api/daily-routine/${id}`, {
        startDateTime: startDate,
        endDateTime: endDate,
        routineType: routineType,
        routineDescription: content
    })
        .then(() => {
            alert("수정되었습니다.");

            updateLastStartDateTime(endDate);

            location.reload();
        })
        .catch(error => {
            alert("수정에 실패했습니다.");

        });
}

/**
 * 데일리 루틴 삭제 API
 *
 * @param id 삭제할 데일리 루틴 ID
 */
export function deleteDailyRoutineApi(id) {
    axios.delete(`/api/daily-routine/${id}`)
        .then(() => {
            alert("삭제되었습니다.");
            location.reload();
        })
        .catch(error => {
            alert("삭제에 실패했습니다.");
        });
}

/**
 * 공부 시간 목록 조회 API
 *
 * @returns {Promise<*>} 성공 시 공부 시간 목록, 실패 시 alert
 */
export function getStudyHoursExceptTodayApi() {
    return axios.get(`/api/daily-routine/study-hours`)
        .then(({data}) => {
            return data.data;
        })
        .catch(error => {
            alert('공부 시간을 불러오는데 실패했습니다.')
            throw error;
        });
}

/**
 * 공부 시간 목록 조회 API (로그인하지 않은 사용자용)
 *
 * @param urlSafeBase64MemberEmail URL-safe 인코딩된 이메일
 * @returns {Promise<*>} 성공 시 공부 시간 목록, 실패 시 error
 */
export function getStudyHoursExceptTodayApiWithoutLogin(urlSafeBase64MemberEmail) {
    return axios.get(`/api/daily-routine/study-hours/without-login?memberEmail=${urlSafeBase64MemberEmail}`)
        .then(({data}) => {
            return data.data;
        })
        .catch(error => {
            console.log('공부 시간을 불러오는데 실패했습니다.', error)
            throw error;
        });
}
