const inputList = document.getElementsByTagName("input");
const length = inputList.length;
for (let i = 0; i < length; i++) {
    if (inputList[i].hasAttribute("lay-verify")) {
        const parentElement = inputList[i].parentElement.parentElement;
        const count = parentElement.childElementCount;
        for (let j = 0; j < count; j++) {
            const child = parentElement.children[j];
            if (child.tagName == "LABEL") {
                child.innerHTML = child.innerText + "<font color='red'>*</font>";
                break;
            }
        }
    }
}