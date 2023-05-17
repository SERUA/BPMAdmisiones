function PbTableCtrl($scope, modalService) {

    this.isArray = Array.isArray;

    this.isClickable = function () {
        return $scope.properties.isBound('selectedRow');
    };

    this.selectRow = function (row) {
        if (this.isClickable()) {
            $scope.properties.selectedRow = row;
        }
    };

    this.isSelected = function (row) {
        return angular.equals(row, $scope.properties.selectedRow);
    }

    function openModal(modalId) {
        modalService.open(modalId);
    }


    $scope.openModal = function(){
        openModal($scope.properties.modalVerSolicitud);
    }
    
    $scope.verPago = function(_row){
        $scope.properties.urlPago = window.location.protocol + "//" 
            + window.location.host 
            + "/portal/resource/app/aspiranteSDAE/ver_pago_estudio/content/?app=sdae&caseId=" 
            + _row.folio;
        openModal($scope.properties.idModalPago);
    }
}