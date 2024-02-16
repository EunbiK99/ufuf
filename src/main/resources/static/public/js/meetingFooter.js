function changeFooterColorByLocation(){
    const footerItemList = document.querySelectorAll(".footer .footerItem");    
    
    let curruntURL = window.location.href;    

    let url = new URL(curruntURL);
    let path = url.pathname;    

    let curruntPageName = path.substring(path.indexOf("/", 1) + 1);    
    
    for(footerItem of footerItemList){
        if(curruntPageName === footerItem.getAttribute("footeritem")){
            const footerItemIcon = footerItem.querySelector(".fi");

            // 여기서 classList는 DOMTokenList 타입인 놈이고 add, remove 매서드를 호출하면 리스트가 변경되면서 반복문이 중단된다함
            const itemClassList = footerItemIcon.classList;
            
            // 그래서 새로운 클래스리스트를 임시로 저장한 후에 변경해야함. 
            const newClassList = [];
            
            for(const className of itemClassList){
                if(className.includes("fi-rr-")){
                    const srClassName = className.replace("fi-rr-", "fi-sr-");
                    newClassList.push(srClassName);                    
                }
                else{
                    newClassList.push(className);
                }
            }
            itemClassList.value = newClassList.join(" ");
            
            const footerItemSpan = footerItem.querySelector(".footerItemSpan");            
            footerItemSpan.setAttribute("style", "color: #FF8827;")
        }
    }
}