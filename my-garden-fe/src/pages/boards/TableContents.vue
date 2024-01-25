<script setup>

</script>

<template>
  <table aria-describedby="contentsList" class="table table-hover text-align-center">
    <thead>
    <tr>
      <th scope="col" style="width: 10%;">번호</th>
      <th scope="col" style="width: 12%;">카테고리</th>
      <th scope="col" style="width: 45%;">제목</th>
      <th scope="col" style="width: 10%;">조회수</th>
      <th scope="col" style="width: 13%;">등록 일시</th>
      <th scope="col" style="width: 10%;">작성자</th>
    </tr>
    </thead>
    <tbody class="table-group-divider">
    <tr>
      <th scope="row">111</th>
      <td>공지</td>
      <td>
        <!--TODO: 제목 클릭시 상세 페이지로 이동, Query Parameter 모두 가지고 이동-->
        <a class="title_link">
          공지사항 제목이 출력됩니다.
        </a>
        <p>NEW</p>
      </td>
      <td>1000</td>
      <td>2024.11.25 12:00</td>
      <td>관리자</td>
    </tr>
    </tbody>
  </table>
</template>

<style scoped>
.text-align-center {
  text-align: center;
}

.title_link {
  color: black;
  text-decoration: none;
}
</style>
