function ($scope) {
    var editor = null;


    function inicializar() {
        setTimeout(function () {
            console.error("no funciona")
            if (editor == null) {
                editor = new nicEditor().panelInstance($scope.properties.id);
                    setInterval(function () {

                        $scope.properties.value = editor.nicInstances[0].getContent();
                        $scope.$apply();
            
                }, 100);
            } else {
                inicializar();
            }
            debugger;
            var b= document.getElementsByClassName("nicEdit-button").length;
            for(var i=0; b>i; i++){
                if(i==1){
                    document.getElementsByClassName("nicEdit-button")[i].innerHTML = "<i class='glyphicon glyphicon-bold'></i>";
                }
                if(i==2){
                    document.getElementsByClassName("nicEdit-button")[i].innerHTML = "<i class='glyphicon glyphicon-italic'></i>";
                }
                if(i==3){
                    document.getElementsByClassName("nicEdit-button")[i].innerHTML = "<i class='glyphicon glyphicon-text-color'></i>";
                }
                if(i==4){
                    document.getElementsByClassName("nicEdit-button")[i].innerHTML = "<i class='glyphicon glyphicon-align-left'></i>";
                }
                if(i==5){
                    document.getElementsByClassName("nicEdit-button")[i].innerHTML = "<i class='glyphicon glyphicon-align-center'></i>";
                }
                if(i==6){
                    document.getElementsByClassName("nicEdit-button")[i].innerHTML = "<i class='glyphicon glyphicon-align-right'></i>";
                }
                if(i==7){
                    document.getElementsByClassName("nicEdit-button")[i].innerHTML = "<i class='glyphicon glyphicon-align-justify'></i>";
                }
                if(i==8){
                    document.getElementsByClassName("nicEdit-button")[i].innerHTML = "<i class='glyphicon glyphicon-list'></i>";
                }
                if(i==9){
                    document.getElementsByClassName("nicEdit-button")[i].innerHTML = "<i class='glyphicon glyphicon-list'></i>";
                }
                if(i==10){
                    document.getElementsByClassName("nicEdit-button")[i].innerHTML = "<i class='glyphicon glyphicon-indent-left'></i>";
                }
                if(i==11){
                    document.getElementsByClassName("nicEdit-button")[i].innerHTML = "<i class='glyphicon glyphicon-indent-right'></i>";
                }
                if(i==12){
                    document.getElementsByClassName("nicEdit-button")[i].innerHTML = "<i class='glyphicon glyphicon-picture'></i>";
                }
                if(i==13){
                    document.getElementsByClassName("nicEdit-button")[i].innerHTML = "<i class='glyphicon glyphicon-cloud-upload'></i>";
                }
                if(i==14){
                    document.getElementsByClassName("nicEdit-button")[i].innerHTML = "<i class='glyphicon glyphicon-link'></i>";
                }
                if(i==15){
                    document.getElementsByClassName("nicEdit-button")[i].innerHTML = "<i class='glyphicon glyphicon-floppy-remove'></i>";
                }
                if(i==16){
                    document.getElementsByClassName("nicEdit-button")[i].innerHTML = "<i class='glyphicon glyphicon-pencil'></i>";
                }
                if(i==17){
                    document.getElementsByClassName("nicEdit-button")[i].innerHTML = "<i class='glyphicon glyphicon-stop'></i>";
                }
            }

        }, 500);
    }
    inicializar();
}