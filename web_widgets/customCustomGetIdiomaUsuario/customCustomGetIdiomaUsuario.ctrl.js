function($scope, $http) {

    $scope.getCommentList = function() {
        debugger;
        var req = {
            method: "GET",
            url: "../API/extension/AnahuacINVPRestGet?url=getIdiomaUsuario&p=0&c=10&username=" + $scope.properties.reload
        };
        return $http(req).success(function(data, status) {
            if(data.data.length === 0){
                $scope.properties.commentList = data.data[0].nombresesion;
                $scope.properties.campusSeleccionado = "ESP";
                localStorage.setItem('idioma', "ESP");
            }else{
                $scope.properties.commentList = data.data[0].nombresesion;
                $scope.properties.campusSeleccionado = data.data[0].idioma;
                localStorage.setItem('idioma', data.data[0].idioma);
            }
                
            }).error(function(data, status) {
                console.log(data);
            })
            .finally(function() {
                //blockUI.stop();
            });
            
    }

    // $scope.getCommentList();

    $scope.$watch("properties.reload", function() {
        if ($scope.properties.reload !== undefined) {
            $scope.getCommentList();
        }
        /*if(($scope.properties.reload === undefined || $scope.properties.reload.length === 0) && $scope.properties.campusSeleccionado !== undefined){
            $("#loading").modal("show");
        }else{
            $("#loading").modal("hide");
        }*/
    });
}