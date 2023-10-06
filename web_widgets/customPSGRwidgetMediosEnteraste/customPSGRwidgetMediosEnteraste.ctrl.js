function ($scope) {
    
    $scope.$watchCollection("[properties.catMediosEnteraste, properties.value]", function(){
        if($scope.properties.catMediosEnteraste && $scope.properties.value.length === 0){
            for(let item in $scope.properties.catMediosEnteraste){
                $scope.properties.value.add({
                    "seleccionado": false,
                    "cat_medio_enteraste": item,
                    "especifique":""
                });
            }
        } 
    });
}