function ($scope, $http) {
    // $scope.url = window.top.location.href = window.location.protocol + "//" + window.location.hostname +"/apps/pg_aspirante/pg_home/";
    $scope.b64File = "";
    $scope.logout = function(){
        let url = "	/bonita/logoutservice?redirect=false";
        var req = {
            method: "POST",
            url: url,
        };
  
        return $http(req).success(function(data){
            let href = window.location.protocol + "//" + window.location.host + "/apps/login/posgrados/";
            window.top.location = href;
        }).error(function(error){
            
        });
    }
    
    
    $scope.pdfCartaPosgrados = function(){
        let url = "../API/extension/DocAPI?pdf=pdfCartaPosgrados&p=0&c=1&caseid=" + $scope.properties.caseid;
        var req = {
            method: "POST",
            url: url,
        };
  /*
        return $http(req).success(function(success){
            $scope.b64File = success.data[0]; 
            
            let b64 = angular.copy($scope.b64File).toString().trim();
            const byteCharacters = atob(b64);
            const byteNumbers = new Array(byteCharacters.length);
            for (let i = 0; i < byteCharacters.length; i++) {
              byteNumbers[i] = byteCharacters.charCodeAt(i);
            }
            const byteArray = new Uint8Array(byteNumbers);
            const blob = new Blob([byteArray], {type: 'application/pdf'});
            let objToDelete = document.getElementById("objetoDocumento");
            if(objToDelete){
                objToDelete.remove();
            }
            const obj2 = document.createElement('object');
            obj2.data = URL.createObjectURL(blob);
            obj2.id = "objetoDocumento";
            obj2.width = "100%";
            obj2.height = "350px";
            document.getElementById("carta").appendChild(obj2);
        }).error(function(error){
            
        });*/
    }

    $scope.pngCartaPosgrados = function(){
        let url = "../API/extension/DocAPI?pdf=pngCartaPosgrados&p=0&c=1&caseid=" + $scope.properties.caseid;
        var req = {
            method: "POST",
            url: url,
        };
  
        return $http(req).success(function(success){
            $scope.b64File = success.data[0]; 
            
            let b64 = angular.copy($scope.b64File).toString().trim();

            // Asignar la cadena Base64 como el origen de la imagen
            document.getElementById('imagen-carta').src = 'data:image/png;base64,' + b64;

        }).error(function(error){
            console.log("Error al convertir la imagen Carta posgrados. " + error.message)
        });
    }
    
    $scope.$watch("properties.caseid", function(){
        if($scope.properties.caseid){
            //$scope.pdfCartaPosgrados();       
            $scope.pngCartaPosgrados()
        }
    })
    
    $scope.asklogout = function(){
         swal({
            "title": "Confirmación",
            "text": "¿Estás seguro que deseas cerrar sesión?",
            icon: "warning",
            buttons: [
                'Cancelar',
                'Si, cerrar sesión'
            ],
            dangerMode: true,
        }).then(function (isConfirm) {
            if (isConfirm) {
                $scope.logout();
            }
        })
    }
}