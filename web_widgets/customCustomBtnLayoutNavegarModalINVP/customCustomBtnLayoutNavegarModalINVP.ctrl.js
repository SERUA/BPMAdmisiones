function PbButtonCtrl($scope, modalService) {
    $scope.changeVariable = function(){
        
        swal({
            title: "Confirmación", 
            text: "Una vez que envies el examen, no podrás cambiar o concluir tus respuestas",
            icon: "warning",
            buttons: [
                'Cancelar',
                'Enviar todo y terminar'
            ]
        }).then(function (isConfirm) {
            if (isConfirm) {
                localStorage.setItem('terminado', "true");
                $scope.properties.navigationVar = $scope.properties.newValue;
                modalService.close();
            } else {
                modalService.close();
            }
        })
            
        // localStorage.setItem('terminado', "true");
        // $scope.properties.navigationVar = $scope.properties.newValue;
    }   
}