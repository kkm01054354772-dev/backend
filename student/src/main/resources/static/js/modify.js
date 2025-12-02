// 삭제 버튼 클릭시
// remove-form을 가져온 후 submit 시키기
document.querySelector(".btn-outline-danger").addEventListener("click", (e) => {
  document.querySelector("[name='remove-form']").submit();
});
