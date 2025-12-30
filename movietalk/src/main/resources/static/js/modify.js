// 삭제 버튼 클릭시 createForm action을 /movie/remove로 변경
document.querySelector(".delete").addEventListener("click", () => {
  document.querySelector("#removeForm").submit();
});

document.querySelectorAll(".uploadResult").forEach((item) => {
  item.addEventListener("click", (e) => {
    e.preventDefault();

    const li = e.target.closest("li");

    if (confirm("정말로 삭제하시겠습니까?")) {
      // 화면에서 이미지 제거
      li.remove();
    }
  });
});
