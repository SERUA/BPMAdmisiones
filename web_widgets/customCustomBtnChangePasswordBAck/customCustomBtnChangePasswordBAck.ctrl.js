function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    'use strict';

    var vm = this;

    this.action = function action() {
        if (validateForm()) {
            loginBonita();
        }
    };

    function validateForm() {
        let validForm = false;
        let errorMessage = "";
        if ($scope.properties.passwordObject.currentPassword === "") {
            validForm = false;
            errorMessage = "El campo Contraseña actual no debe ir vacío.";
        } else if ($scope.properties.passwordObject.newPassword === "") {
            validForm = false;
            errorMessage = "El campo Nueva contraseña no debe ir vacío.";
        } else if ($scope.properties.passwordObject.confirmPassword === "") {
            validForm = false;
            errorMessage = "El campo Confirmar contraseña no debe ir vacío.";
        } else if ($scope.properties.passwordObject.confirmPassword !== $scope.properties.passwordObject.newPassword) {
            validForm = false;
            errorMessage = "Las contraseñas no coinciden";
        } else if ($scope.properties.passwordObject.confirmPassword !== $scope.properties.passwordObject.newPassword) {
            validForm = false;
            errorMessage = "Las contraseñas no coinciden";
        } else if ($scope.properties.passwordObject.newPassword < 8 || $scope.properties.passwordObject.newPassword > 16) {
            validForm = false;
            errorMessage = "El formato de la contraseña no es correcto. debe ser entre 8 y 16 caracteres.";
        } else {
            validForm = true;
            errorMessage = "";
        }

        if (!validForm) {
            swal("Atención", errorMessage, "warning");
        }
        return validForm;
    }

    /**
     * Execute a get/post request to an URL
     * It also bind custom data from success|error to a data
     * @return {void}
     */
    function loginBonita() {
        vm.busy = true;

        let data = {
            username: $scope.properties.user.userName,
            password: $scope.properties.passwordObject.currentPassword
        };

        let urlLoginBonita = "/bonita/loginservice?&redirect=false&username=" + data.username + "&password=" + data.password;

        var req = {
            method: "POST",
            url: urlLoginBonita,
            data: angular.copy(data),
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
        };

        return $http(req).success(function(data, status) {
                updatePassword();
            })
            .error(function(data, status) {
                console.log(data);
                swal("Error", "Contraseña incorrecta", "error");
            })
            .finally(function() {
                vm.busy = false;
            });
    }

    /**
     * Execute a get/post request to an URL
     * It also bind custom data from success|error to a data
     * @return {void}
     */
    function updatePassword() {
        vm.busy = true;
        let data = {
            "password": $scope.properties.passwordObject.newPassword
        }
        let urlUpdatePassword = "../API/identity/user/" + $scope.properties.user.id;

        var req = {
            method: "PUT",
            url: urlUpdatePassword,
            data: angular.copy(data)
        };

        return $http(req).success(function(data, status) {
                swal("Ok", "Contraseña actualizada, cerrando sesión.", "success");
                setTimeout(() => {
                    logout();
                }, 2000);
            })
            .error(function(data, status) {
                swal("Error", "Algo ha fallado: " + data.message, "error");
            })
            .finally(function() {
                vm.busy = false;
            });
    }

    /**
     * Execute a get/post request to an URL
     * It also bind custom data from success|error to a data
     * @return {void}
     */
    function logout(a) {
        vm.busy = true;
        let urlLogout = "../../../logoutservice?redirect=false";
        let data = {};
        var req = {
            method: "POST",
            url: urlLogout,
            data: angular.copy(data)
        };

        return $http(req).success(function(data, status) {
                let href = window.location.protocol + "//" + window.location.host + "/apps/login/administrativo";
                window.top.location = href;
            })
            .error(function(data, status) {

            })
            .finally(function() {
                vm.busy = false;
            });
    }
    $scope.validatePassword = function(password) {
        var re = /(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\W)/s
        var size = password.length
        return re.test(password) && size >= 8 && size <= 16
    }
    $scope.validatePasswordLC = function(password) {
        var re = /(?=.*[a-z])/s
        return re.test(password)
    }
    $scope.validatePasswordUC = function(password) {
        var re = /(?=.*[A-Z])/s
        return re.test(password)
    }
    $scope.validatePasswordNumber = function(password) {
        var re = /(?=.*\d)/s
        return re.test(password)
    }
    $scope.validatePasswordEspecial = function(password) {
        var re = /(?=.*\W)/s
        return re.test(password)
    }
    $scope.validatePasswordSize = function(password) {
        var size = password.length
        return size >= 8 && size <= 16
    }
}