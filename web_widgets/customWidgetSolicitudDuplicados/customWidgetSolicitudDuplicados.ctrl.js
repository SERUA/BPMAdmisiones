function duplicados($scope, modalService) {
    this.trabaja = function trabaja() {
        console.log("has algo");
        openModal($scope.properties.modalid);
    }
    
    $scope.$watch("properties.findByCorreoElectronico", function(){
        if($scope.properties.findByCorreoElectronico !== undefined){
            if($scope.properties.findByCorreoElectronico.length === 1){
                $scope.properties.value = true;
            }
        }
    });
    
    function openModal(modalid) {

        modalService.open(modalid);
    }

    function closeModal(shouldClose) {
        if (shouldClose)
            modalService.close();
    }
}