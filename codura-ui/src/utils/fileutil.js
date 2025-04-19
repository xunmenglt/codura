const fileInputEl = document.createElement("input");
fileInputEl.setAttribute("type", "file");
fileInputEl.style.display = "none";
 
/**
 * 选择文件
 * @param type 文件类型：".xlsx"
 * @returns {fileData} file类型数据
 */
export function selectFile(type) {
    fileInputEl.setAttribute("accept", type ? type : "*");
    fileInputEl.click();
    return new Promise((resolve, reject) => {
        fileInputEl.addEventListener('change', onSelectChanged);
        function onSelectChanged() {
            if (fileInputEl?.files && fileInputEl?.files.length) {
                let fileData = fileInputEl.files[0];
                resolve(fileData);
            } else {
                reject()
            }
        }
        fileInputEl.addEventListener('cancel', onCanceled);
        function onCanceled(){
            reject();
        }
    });
    
}
function clearInputFile(){ 
    fileInputEl.value = null
}