function ($scope) {
    // $scope.url = window.top.location.href = window.location.protocol + "//" + window.location.hostname +"/apps/pg_aspirante/pg_home/";
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
}