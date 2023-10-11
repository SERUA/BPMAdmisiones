function($scope, $http) {
    
    $http.get("../API/system/session/1").success(function(_data){
        let url = "../API/bpm/humanTask?p=0&c=10&f=assigned_id=" + _data.user_id;
       
        $http.get(url).success(function(_data2){
            $scope.properties.taskid = _data2[0].id;
        }).error(function(){
            console-log("error al obtener las tareas");
        });
    });
}