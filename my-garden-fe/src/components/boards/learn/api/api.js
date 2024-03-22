import axios from "axios";

/**
 * TIL 목록 조회 API
 *
 * @param parameters 조회 파라미터
 * @returns {Promise<axios.AxiosResponse<any> | void>} 성공 시 TIL 목록, 실패 시 alert
 */
export function getLearnBoardListApi(parameters) {
    let queryParameter = '';
    if (parameters) {
        queryParameter = Object.entries(parameters)
            .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`)
            .join('&');
    }

    return axios.get(`/api/boards/learn/list?${queryParameter}`)
        .then(({data}) => {
            return data.data;
        })
        .catch(error => {
            alert('TIL 목록을 불러오는데 실패했습니다.')
        });
}

/**
 * TIL 분류 목록 조회 API
 *
 * @param boardType 게시판 타입
 * @returns {Promise<axios.AxiosResponse<any> | void>} 성공 시 TIL 분류 목록, 실패 시 alert
 */
export function getLearnBoardCategoryApi(boardType) {
    return axios.get(`/api/boards/categories?boardType=${boardType}`)
        .then(({data}) => {
            return data.data;
        })
        .catch(error => {
            alert('TIL 분류를 불러오는데 실패했습니다.')
        });
}

/**
 * TIL 상세 조회 API
 *
 * @param boardId 게시글 ID
 * @returns {Promise<axios.AxiosResponse<any> | void>} 성공 시 TIL 상세 내용, 실패 시 alert
 */
export function getLearnBoardViewApi(boardId) {
    return axios.get(`/api/boards/learn/${boardId}`)
        .then(({data}) => {
            return data.data;
        })
        .catch(error => {
            alert('TIL을 불러오는데 실패했습니다.')
        });
}

/**
 * TIL 삭제 API
 *
 * @param boardId 게시글 ID
 * @returns {Promise<axios.AxiosResponse<any> | void>} 성공 시 '삭제한 게시글 ID', 실패 시 alert
 */
export function deleteLearnBoardApi(boardId) {
    return axios.delete(`/api/boards/learn/${boardId}`)
        .then(({data}) => {
            alert("삭제되었습니다.");
        })
        .catch(error => {
            alert('TIL을 삭제하는데 실패했습니다.')
        });
}

/**
 * 댓글 조회 API
 */
export function getLearnBoardCommentsApi(boardType, boardId) {
    return axios.get(`/api/boards/comments/${boardType}/${boardId}`)
        .then(({data}) => {
            return data.data;
        })
        .catch(error => {
            alert('댓글을 불러오는데 실패했습니다.')
        });
}

/**
 * 댓글 등록 API
 */
export function postLearnBoardCommentApi(boardType, boardId, comment) {
    return axios.post(`/api/boards/comments/${boardType}/${boardId}`, {comment})
        .then(({data}) => {
            return data.data;
        })
        .catch(error => {
            alert('댓글을 등록하는데 실패했습니다.')
        });
}

/**
 * 댓글 삭제 API
 */
export function deleteLearnBoardCommentApi(boardType, boardId, commentId) {
    return axios.delete(`/api/boards/comments/${boardType}/${boardId}/${commentId}`)
        .then(({data}) => {
            return data.data;
        })
        .catch(error => {
            alert('댓글을 삭제하는데 실패했습니다.')
        });
}
