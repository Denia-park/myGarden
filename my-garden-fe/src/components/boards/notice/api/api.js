import axios from "axios";

/**
 * 공지사항 중요 목록 조회 API
 *
 * @returns {Promise<axios.AxiosResponse<any> | void>} 성공 시 중요 공지사항 목록, 실패 시 alert
 */
export function getNoticeImportantBoardListApi() {
    return axios.get('/api/boards/notice/important')
        .then(({data}) => {
            return data.data;
        })
        .catch(error => {
            alert('중요 공지사항 목록을 불러오는데 실패했습니다.')
        });
}

/**
 * 공지사항 목록 조회 API
 *
 * @param parameters 조회 조건
 * @returns {Promise<axios.AxiosResponse<any> | void>} 성공 시 공지사항 목록, 실패 시 alert
 */
export function getNoticeBoardListApi(parameters) {
    let queryParameter = '';
    if (parameters) {
        queryParameter = Object.entries(parameters)
            .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`)
            .join('&');
    }

    return axios.get(`/api/boards/notice/list?${queryParameter}`)
        .then(({data}) => {
            return data.data;
        })
        .catch(error => {
            alert('공지사항 목록을 불러오는데 실패했습니다.')
        });
}

/**
 * 공지사항 분류 목록 조회 API
 *
 * @param boardType 게시판 타입
 * @returns {Promise<axios.AxiosResponse<any> | void>}
 */
export function getNoticeBoardCategoryApi(boardType) {
    return axios.get(`/api/boards/categories?boardType=${boardType}`)
        .then(({data}) => {
            return data.data;
        })
        .catch(error => {
            alert('공지사항 분류를 불러오는데 실패했습니다.')
        });
}

/**
 * 공지사항 상세 조회 API
 *
 * @param boardId 게시글 ID
 * @returns {Promise<axios.AxiosResponse<any> | void>} 성공 시 공지사항 상세 내용, 실패 시 alert
 */
export function getNoticeBoardViewApi(boardId) {
    return axios.get(`/api/boards/notice/${boardId}`)
        .then(({data}) => {
            return data.data;
        })
        .catch(error => {
            alert('공지사항을 불러오는데 실패했습니다.')
        });
}

/**
 * 공지사항 삭제 API
 *
 * @param boardId 게시글 ID
 * @returns {Promise<axios.AxiosResponse<any> | void>} 성공 시 '삭제된 공지사항 ID', 실패 시 alert
 */
export function deleteNoticeBoardApi(boardId) {
    return axios.delete(`/api/boards/notice/${boardId}`)
        .then(({data}) => {
            alert("삭제되었습니다.");
        })
        .catch(error => {
            alert('공지사항을 삭제하는데 실패했습니다.')
        });
}
