// 삭제 버튼 클릭 시
// form action ="위치" 지정

document.querySelector(".btn-outline-danger").addEventListener("click", () => {
  const form = document.querySelector("#modify-form");

  form.action = "/memo/remove";
  form.submit();
});
