var commonLib = commonLib || {};
/**
* 1. 파일 업로드
*/
commonLib.fileManager = {
    /**
    * 파일 업로드 처리
    */
    upload(files) {
        try{
            if(!files || files.length == 0){
                throw new Error("업로드할 파일을 선택하세요.");
            }

            // gid
            const gidEl = document.querySelector("[name='gid']");
            if(!gidEl || !gidEl.value.trim()){
                throw new Error("gid가 누락되었습니다.");
            }

            const gid = gidEl.value.trim();

            const formData = new FormData(); // 기본 content-type : multipart/form-data
            formData.append("gid", gid);

            for(const file of files){
                formData.append("file", file);
            }

            const { ajaxLoad } = commonLib;
            ajaxLoad("POST", "/api/file", formData, "json").then(res => {
                // 요청 성공 시
                // console.log(res);
                if(res && res.success){
                    if(typeof parent.callbackFileupload == 'function'){
                        parent.callbackFileupload(res.data); // 회원가입에서 적용할 수 있게
                    }

                } else { // 파일 업로드 실패 시
                    if(res) alert(res.message);
                }
            }).catch(err => console.error(err));

        } catch(err){
            alert(err.message);
            console.error(err);
        }

    }
};


// 이벤트 처리
window.addEventListener("DOMContentLoaded", function() {
    const uploadFiles = document.getElementsByClassName("upload_files");

    const fileEl = document.createElement("input");
    fileEl.type="file"; // 메모리상에만 존재

    for (const el of uploadFiles) {
        el.addEventListener("click", function() {
            fileEl.click();
        });
    }

    // 파일 선택 시 이벤트 처리
    fileEl.addEventListener("change", function(e) {
        // console.dir(e.target.files);
        commonLib.fileManager.upload(e.target.files);
    });

});