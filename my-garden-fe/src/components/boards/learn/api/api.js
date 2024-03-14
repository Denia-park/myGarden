import axios from "axios";

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

export function getLearnBoardCategoryApi(boardType) {
    return axios.get(`/api/boards/categories?boardType=${boardType}`)
        .then(({data}) => {
            return data.data;
        })
        .catch(error => {
            alert('TIL 분류를 불러오는데 실패했습니다.')
        });
}

export function getLearnBoardViewApi(boardId) {
    return axios.get(`/api/boards/learn/${boardId}`)
        .then(({data}) => {
            return data.data;
        })
        .catch(error => {
            alert('TIL을 불러오는데 실패했습니다.')
        });
}

export function deleteLearnBoardApi(boardId) {
    return axios.delete(`/api/boards/learn/${boardId}`)
        .then(({data}) => {
            return data.data;
        })
        .catch(error => {
            alert('TIL을 삭제하는데 실패했습니다.')
        });
}
