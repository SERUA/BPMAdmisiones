function ($scope, $http) {
    $scope.$watch("properties.userData", function(){
       if($scope.properties.userData){
           let url = "/API/bpm/humanTask?p=0&c=10&f=assigned_id=" + $scope.properties.userData.user_id;
           
           $http.get("url").success(function(data){
              $scope.properties.taskid = data[0].id;
           }).error(function(){
               console-log("error al obtener las tareas")
           });
       } 
    });
}