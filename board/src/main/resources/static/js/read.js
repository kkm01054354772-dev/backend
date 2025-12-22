const replyList = document.querySelector(".replyList");
const url = `http://localhost:8080/replies`;

// -------------------------- READ
// -- 댓글 목록 가져오기
const loadReply = () => {
  fetch(`${url}/board/${bno}`)
    .then((res) => {
      if (!res.ok) {
        throw new Error(`에러 발생 ${res.status}`);
      }
      return res.json();
    })
    .then((data) => {
      console.log(data);

      // 댓글 개수 보여주기
      document.querySelector(".row .card:nth-child(2) .card-title span").innerHTML = data.length;
      // replyList.previousElementSibling.firstElementChild.innerHTML=data.length;

      let result = "";
      // data를 댓글 영역에 보여주기
      data.forEach((reply) => {
        result += `<div class="d-flex justify-content-between my-2 border-bottom reply-row" data-rno="${reply.rno}" data-email="${reply.replyerEmail}">
              <div class="p-3">
                <img src="/img/user.png" alt="" class="rounded-circle mx-auto d-block" style="width: 60px; height: 60px" />
              </div>
              <div class="flex-grow-1 align-self-center">
                <div class="replyer">${reply.replyerName}</div>
                <div>
                  <span class="small">${reply.text}</span>
                </div>
                <div>
                  <span class="small">${formatDate(reply.createDate)}</span>
                </div>
              </div>
              <div class="d-flex flex-column align-self-center">
                <div class="mb-2">
                  <button class="btn btn-outline-danger btn-sm">삭제</button>
                </div>
                <div class="mb-2">
                  <button class="btn btn-outline-primary btn-sm">수정</button>
                </div>
              </div>
            </div>`;
      });
      replyList.innerHTML = result;
    });
};

loadReply();
// -------------------------- CREATE / UPDATE
// -- 댓글 작성 클릭 시 = replyForm submit 발생 시
document.querySelector("#replyForm").addEventListener("submit", (e) => {
  // submit 기능 중지
  e.preventDefault();
  const form = e.target;
  const rno = form.rno.value;
  // {
  //     "text": "reply....추가",
  //     "replyer": "guest....1",
  //     "bno": 301
  // }
  const reply = {
    rno: rno,
    text: form.text.value,
    replyerEmail: form.replyerEmail.value,
    bno: bno,
  };

  // new or modify => rno value 존재 여부
  if (!rno) {
    // new
    fetch(`${url}/new`, {
      method: "POST",
      headers: {
        "X-CSRF-TOKEN": csrfVal,
        "Content-Type": "application/json",
      },
      body: JSON.stringify(reply),
    })
      .then((res) => {
        if (!res.ok) {
          throw new Error(`error! ${res.status}`);
        }
        // json body 추출
        return res.json();
      })
      .then((data) => {
        console.log(data);
        if (data) {
          Swal.fire({
            title: "댓글 작성 완료",
            icon: "success",
            draggable: true,
          });
        }
        // form.replyer.value = "";
        form.text.value = "";
        // 댓글 불러오기
        loadReply();
      })
      .catch((err) => console.log(err));
  } else {
    // modify
    fetch(`${url}/${rno}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(reply),
    })
      .then((res) => {
        if (!res.ok) {
          throw new Error(`error! ${res.status}`);
        }
        // json body 추출
        return res.json();
      })
      .then((data) => {
        console.log(data);
        if (data) {
          Swal.fire({
            title: "댓글 수정 완료",
            icon: "success",
            draggable: true,
          });
        }
        form.replyer.value = "";
        form.text.value = "";
        form.rno.value = "";

        form.rbtn.innerHTML = "댓글 작성";
        // 댓글 불러오기
        loadReply();
      })
      .catch((err) => console.log(err));
  }
});

// -- 날짜/시간 2025/12/16 12:20
const formatDate = (data) => {
  const date = new Date(data);

  return date.getFullYear() + "/" + (date.getMonth() + 1) + "/" + date.getDate() + " " + date.getHours() + ":" + date.getMinutes();
};

// -------------------------- DELETE
// 방법 1. 삭제 버튼 클릭 시
// document.querySelectorAll(".btn-outline-danger").forEach((btn) => {
//   btn.addEventListener("click", (e) => {
//     const targetBtn = e.target;

//     const rno = targetBtn.closest(".reply-row").dataset.rno;
//   });
// });
// 방법 2. 이벤트 버블링 이용
replyList.addEventListener("click", (e) => {
  console.log(e.target); // 어느 버튼의 이벤트인가?
  const btn = e.target;
  // 가까운 부모를 검색
  // data- 접근 : dataset
  const rno = btn.closest(".reply-row").dataset.rno;
  console.log("rno", rno);
  // 삭제 or 수정 버튼 클릭
  if (btn.classList.contains("btn-outline-danger")) {
    if (!confirm("정말로 삭제하시겠습니까?")) return;

    // true인 경우 삭제(fetch)
    fetch(`${url}/${rno}`, {
      method: "DELETE",
    })
      .then((res) => {
        if (!res.ok) {
          throw new Error(`error! ${res.status}`);
        }
        // text 추출
        return res.text();
      })
      .then((data) => {
        console.log(data);
        if (data) {
          Swal.fire({
            title: "데이터 삭제 완료",
            icon: "success",
            draggable: true,
          });
        }
        // 댓글 목록 다시 가져오기
        loadReply();
      })
      .catch((err) => console.log(err));
  } else if (btn.classList.contains("btn-outline-primary")) {
    // rno를 이용해 reply 가져오기
    const form = document.querySelector("#replyForm");
    fetch(`${url}/${rno}`)
      .then((res) => {
        if (!res.ok) {
          throw new Error(`error! ${res.status}`);
        }
        // json body 추출
        return res.json();
      })
      .then((data) => {
        console.log(data);

        // 가져온 reply를 replyForm에 보여주기
        form.rno.value = data.rno;
        form.replyer.value = data.replyer;
        form.text.value = data.text;

        // 댓글 작성 => 댓글 수정버튼으로 변경
        form.rbtn.innerHTML = "댓글 수정";
      })
      .catch((err) => console.log(err));
  }
});
