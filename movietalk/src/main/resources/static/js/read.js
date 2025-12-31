const baseUrl = "/reviews";
const reviewArea = document.querySelector(".reviewList");
const reviewCnt = document.querySelector(".review-cnt");
const reviewForm = document.querySelector("#reviewForm");

// -- 날짜/시간 2025/12/31 12:30
const formatDate = (data) => {
  const date = new Date(data);

  return date.getFullYear() + "/" + (date.getMonth() + 1) + "/" + date.getDate() + " " + date.getHours() + ":" + date.getMinutes();
};

// 전체 리뷰 가져오기
const reviewList = () => {
  fetch(`${baseUrl}/${mno}/all`)
    .then((res) => {
      if (!res.ok) {
        throw new Error("에러발생");
      }
      return res.json();
    })
    .then((data) => {
      // 화면 작업
      console.log(data);

      let result = "";
      data.forEach((review) => {
        result += `<div class="d-flex justify-content-between py-2 border-bottom review-row" data-rno="${review.rno}">`;
        result += `<div class="flex-grow-1 align-self-center">`;
        result += `<div><span class="font-semibold">${review.text}</span></div>`;
        result += `<div class="small text-muted"><span class="d-inline-block mr-3">${review.nickname}</span>`;
        result += `평점 : <span class="grade">${review.grade}</span><div class="starrr"></div></div>`;
        result += `<div class="text-muted"><span class="small">${formatDate(review.createDate)}</span></div></div>`;
        result += `<div class="d-flex flex-column align-self-center">`;
        result += `<div class="mb-2"><button class="btn btn-outline-danger btn-sm">삭제</button></div>`;
        result += `<div><button class="btn btn-outline-success btn-sm">수정</button></div>`;
        result += `</div></div>`;
      });

      reviewArea.innerHTML = result;
    })
    .catch((e) => console.error(e));
};

reviewList();

// 특정 리뷰 가져오기
const reviewGet = (rno) => {
  fetch(`${baseUrl}/${mno}/${rno}`)
    .then((res) => {
      if (!res.ok) {
        throw new Error("에러발생");
      }
      return res.json();
    })
    .then((data) => {
      // 화면 작업
      console.log("get");
      console.log(data);

      reviewForm.nickname.value = data.nickname;
      reviewForm.text.value = data.text;
      reviewForm.rno.value = data.rno;
      reviewForm.mid.value = data.mid;
      reviewForm.mno.value = data.mno;
      reviewForm.querySelector(".starrr a:nth-child(" + data.grade + ")").click();
    })
    .catch((e) => console.error(e));
};
// 이벤트 버블링 이용하여 수정 / 삭제 버튼 클릭 시
reviewArea.addEventListener("click", (e) => {
  // 어느 버튼의 이벤트? 수정 or 삭제
  const btn = e.target;
  const rno = btn.closest(".review-row").dataset.rno;
  if (btn.classList.contains("btn-outline-danger")) {
    //삭제
  } else {
    //수정
    reviewGet(rno);
  }
});

// 이미지 Modal로 띄우기
const imgModal = document.getElementById("imgModal");
if (imgModal) {
  imgModal.addEventListener("show.bs.modal", (e) => {
    // Modal 창을 띄우는 li 요소 찾기
    const posterLi = e.relatedTarget;
    // li의 data-* 값 가져오기
    const filePath = posterLi.getAttribute("data-file");

    // Update the modal's content.
    const modalTitle = imgModal.querySelector(".modal-title");
    const modalBody = imgModal.querySelector(".modal-body");

    modalTitle.textContent = `${title}`;
    modalBody.innerHTML = `<img src="/upload/display?fileName=${filePath}" style="width:100%">`;
  });
}
