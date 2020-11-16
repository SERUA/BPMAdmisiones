function ($scope) {
     $(function(){
        window.$crisp=[];
        //window.CRISP_WEBSITE_ID="5760f1e1-1fbb-432c-ae13-f743ecb1d17f";
        window.CRISP_WEBSITE_ID="d9614e42-c9fc-4cfa-9cd6-109d47c98464";
        (
            function(){
                d=document;
                s=d.createElement("script");
                s.src="https://client.crisp.chat/l.js";
                s.async=1;
                d.getElementsByTagName("head")[0].appendChild(s);
            }
        )();
   });
}