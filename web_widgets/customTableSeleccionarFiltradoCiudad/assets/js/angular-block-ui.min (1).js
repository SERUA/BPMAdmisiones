/*!
   angular-block-ui v0.1.1
   (c) 2014 (null) McNull https://github.com/McNull/angular-block-ui
   License: MIT
*/
! function(t) {
    var e = t.module("blockUI", []);
    e.config(["$provide", "$httpProvider", function(t, e) {
        t.decorator("$exceptionHandler", ["$delegate", "$injector", function(t, e) {
            var n, o;
            return function(r, c) {
                if (o = o || e.get("blockUIConfig"), o.resetOnException) try { n = n || e.get("blockUI"), n.instances.reset() } catch (i) { console.log("$exceptionHandler", r) }
                t(r, c)
            }
        }]), e.interceptors.push("blockUIHttpInterceptor")
    }]), e.run(["$document", "blockUIConfig", "$templateCache", function(t, e, n) { e.autoInjectBodyBlock && t.find("body").attr("block-ui", "main"), e.template && (e.templateUrl = "$$block-ui-template$$", n.put(e.templateUrl, e.template)) }]), e.directive("blockUiContainer", ["blockUIConfig", "blockUiContainerLinkFn", function(t, e) { return { scope: !0, restrict: "A", templateUrl: t.templateUrl, compile: function() { return e } } }]).factory("blockUiContainerLinkFn", ["blockUI", "blockUIUtils", function() {
        return function(t, e) {
            var n = e.inheritedData("block-ui");
            if (!n) throw new Error("No parent block-ui service instance located.");
            t.state = n.state()
        }
    }]), e.directive("blockUi", ["blockUiCompileFn", function(t) { return { scope: !0, restrict: "A", compile: t } }]).factory("blockUiCompileFn", ["blockUiPreLinkFn", function(t) { return function(e) { return e.append('<div block-ui-container class="block-ui-container"></div>'), { pre: t } } }]).factory("blockUiPreLinkFn", ["blockUI", "blockUIUtils", "blockUIConfig", function(t, e, n) {
        return function(o, r, c) {
            r.hasClass("block-ui") || r.addClass(n.cssClass), c.$observe("blockUiMessageClass", function(t) { o.$_blockUiMessageClass = t });
            var i = c.blockUi || "_" + o.$id,
                a = t.instances.get(i);
            if ("main" === i) var l = o.$on("$viewContentLoaded", function() { l(), o.$on("$locationChangeStart", function(t) { a.state().blockCount > 0 && t.preventDefault() }) });
            else {
                var s = r.inheritedData("block-ui");
                s && (a._parent = s)
            }
            o.$on("$destroy", function() { a.release() }), a.addRef(), o.$_blockUiState = a.state(), o.$watch("$_blockUiState.blocking", function(t) { r.attr("aria-busy", !!t), r.toggleClass("block-ui-visible", !!t) }), o.$watch("$_blockUiState.blockCount > 0", function(t) { r.toggleClass("block-ui-active", !!t) });
            var u = c.blockUiPattern;
            if (u) {
                var f = e.buildRegExp(u);
                a.pattern(f)
            }
            r.data("block-ui", a)
        }
    }]), e.constant("blockUIConfig", { templateUrl: "angular-block-ui/angular-block-ui.ng.html", delay: 250, message: '<div class="sk-chase"> <div class="sk-chase-dot"></div> <div class="sk-chase-dot"></div> <div class="sk-chase-dot"></div> <div class="sk-chase-dot"></div> <div class="sk-chase-dot"></div> <div class="sk-chase-dot"></div> </div>', autoBlock: !0, resetOnException: !0, requestFilter: t.noop, autoInjectBodyBlock: !0, cssClass: "block-ui block-ui-anim-fade" }), e.factory("blockUIHttpInterceptor", ["$q", "$injector", "blockUIConfig", "$templateCache", function(t, e, n, o) {
        function r() { a = a || e.get("blockUI") }

        function c(t) { n.autoBlock && t && !t.$_noBlock && t.$_blocks && (r(), t.$_blocks.stop()) }

        function i(e) { try { c(e.config) } catch (n) { console.log("httpRequestError", n) } return t.reject(e) }
        var a;
        return {
            request: function(t) {
                if (n.autoBlock && ("GET" != t.method || !o.get(t.url))) {
                    var e = n.requestFilter(t);
                    e === !1 ? t.$_noBlock = !0 : (r(), t.$_blocks = a.instances.locate(t), t.$_blocks.start(e))
                }
                return t
            },
            requestError: i,
            response: function(t) { return c(t.config), t },
            responseError: i
        }
    }]), e.factory("blockUI", ["blockUIConfig", "$timeout", "blockUIUtils", "$document", function(e, n, o, r) {
        function c(c) {
            var a, s = this,
                u = { id: c, blockCount: 0, message: e.message, blocking: !1 },
                f = [];
            this._id = c, this._refs = 0, this.start = function(c) {
                c = u.blockCount > 0 ? c || u.message || e.message : c || e.message, u.message = c, u.blockCount++;
                var i = t.element(r[0].activeElement);
                i.length && o.isElementInBlockScope(i, s) && (s._restoreFocus = i[0], n(function() { s._restoreFocus && s._restoreFocus.blur() })), a || (a = n(function() { a = null, u.blocking = !0 }, e.delay))
            }, this._cancelStartTimeout = function() { a && (n.cancel(a), a = null) }, this.stop = function() { u.blockCount = Math.max(0, --u.blockCount), 0 === u.blockCount && s.reset(!0) }, this.message = function(t) { u.message = t }, this.pattern = function(t) { return void 0 !== t && (s._pattern = t), s._pattern }, this.reset = function(e) { s._cancelStartTimeout(), u.blockCount = 0, u.blocking = !1, !s._restoreFocus || r[0].activeElement && r[0].activeElement !== i[0] || (s._restoreFocus.focus(), s._restoreFocus = null); try { e && t.forEach(f, function(t) { t() }) } finally { f.length = 0 } }, this.done = function(t) { f.push(t) }, this.state = function() { return u }, this.addRef = function() { s._refs += 1 }, this.release = function() {--s._refs <= 0 && l.instances._destroy(s) }
        }
        var i = r.find("body"),
            a = [];
        a.get = function(t) { if (!isNaN(t)) throw new Error("BlockUI id cannot be a number"); var e = a[t]; return e || (e = a[t] = new c(t), a.push(e)), e }, a._destroy = function(e) {
            if (t.isString(e) && (e = a[e]), e) {
                e.reset();
                var n = o.indexOf(a, e);
                a.splice(n, 1), delete a[e.state().id]
            }
        }, a.locate = function(t) {
            var e = [];
            o.forEachFnHook(e, "start"), o.forEachFnHook(e, "stop");
            for (var n = a.length; n--;) {
                var r = a[n],
                    c = r._pattern;
                c && c.test(t.url) && e.push(r)
            }
            return 0 === e.length && e.push(l), e
        }, o.forEachFnHook(a, "reset");
        var l = a.get("main");
        return l.addRef(), l.instances = a, l
    }]), e.factory("blockUIUtils", function() {
        var e = t.element,
            n = {
                buildRegExp: function(t) { var e, n = t.match(/^\/(.*)\/([gim]*)$/); if (!n) throw Error("Incorrect regular expression format: " + t); return e = new RegExp(n[1], n[2]) },
                forEachFn: function(t, e, n) {
                    for (var o = t.length; o--;) {
                        var r = t[o];
                        r[e].apply(r, n)
                    }
                },
                forEachFnHook: function(t, e) { t[e] = function() { n.forEachFn(this, e, arguments) } },
                isElementInBlockScope: function(t, e) {
                    for (var n = t.inheritedData("block-ui"); n;) {
                        if (n === e) return !0;
                        n = n._parent
                    }
                    return !1
                },
                findElement: function(t, o, r) {
                    var c = null;
                    if (o(t)) c = t;
                    else {
                        var i;
                        i = r ? t.parent() : t.children();
                        for (var a = i.length; !c && a--;) c = n.findElement(e(i[a]), o, r)
                    }
                    return c
                },
                indexOf: function(t, e, n) {
                    for (var o = n || 0, r = t.length; r > o; o++)
                        if (t[o] === e) return o;
                    return -1
                }
            };
        return n
    }), t.module("blockUI").run(["$templateCache", function(t) { t.put("angular-block-ui/angular-block-ui.ng.html", '<div class="block-ui-overlay"></div><div class="block-ui-message-container" aria-live="assertive" aria-atomic="true"><div class="block-ui-message" ng-class="$_blockUiMessageClass" ng-bind-html="state.message"></div></div>') }])
}(angular);
//# sourceMappingURL=angular-block-ui.min.js.map