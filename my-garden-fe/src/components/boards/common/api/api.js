import axios from "axios";
import {router} from "@/scripts/router.js";

/**
 * 게시글 등록 & 수정 API
 * <br><br>
 * - 게시글 ID가 있으면 수정, 없으면 등록
 * - 등록 및 수정 성공 시 게시판 목록으로 이동
 *
 * @param boardType 게시판 타입
 * @param board 등록할 게시글 정보
 * @param boardId 게시글 ID
 */
export function postBoardApi(boardType, board, boardId) {
    if (boardId) {
        return axios.put(`/api/boards/${boardType}/${boardId}`, board)
            .then(() => {
                alert("수정되었습니다.");
                router.push(`/boards/${boardType}`);
            })
            .catch(error => {
                alert("수정에 실패했습니다.");
            });
    }

    return axios.post(`/api/boards/${boardType}`, board)
        .then(() => {
            alert("등록되었습니다.");
            router.push(`/boards/${boardType}`);
        })
        .catch(error => {
            alert("등록에 실패했습니다.");
        });
}
