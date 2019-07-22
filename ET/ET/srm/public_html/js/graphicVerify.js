/* 
 * type :  blend-數字文字混合、number-纯數字、letter-纯文字
 * inputId : 輸入驗證碼的欄位<input type="text">之 DOM ID
 * refreshId : 刷新驗證碼的元件之DOM ID
 * submitId : 驗證SUBMIT綁定元件之 DOM ID
 */
!(function ($, window, document) {

    function GraphicCode(element, options) {
        /* *Create  GraphicCode object，receive options as parameters. **/
        this.$element = element;

        this.defaults = {
            canvasId: "verifyCanvas", 
            width: "100", 
            height: "30", 
            type: "blend", 
            code: "",
            codeLength: 4,
            inputId: 'code-input-text',
            refreshId: 'refresh-btn',
            submitId: 'check-btn',
            success: function () {},
            error: function () {}
        };
        this.options = $.extend({}, this.defaults, options);
    }

    GraphicCode.prototype = {
        version: '1.0.0',
        code: '',
        txtArr: [],
        htmlDoms: {},
        canvasContent: {},

        _init: function () {
            this.loadDom();
            this.refresh();

            var _this = this;
            this.htmlDoms.code_canvas.on('click', function () {
                _this.refresh();
            });
            this.htmlDoms.refresh_btn.on('click', function () {
                _this.refresh();
            });
            this.htmlDoms.code_btn.on('click', function () {
                _this.validate(_this.htmlDoms.input_text.val()) ? _this.options.success() : _this.options.error();
                _this.refresh();
            });
        },
        loadDom: function () {

            var canvas = $('<canvas/>', {
                id: this.options.canvasId
            }).prop({
                width: this.options.width,
                height: this.options.height,
                innerHTML: 'canvas'
            }).css({
                cursor: 'pointer'
            });

            this.$element.css({
                width: this.options.width,
                height: this.options.height,
                display: 'inline-block'
            }).append(canvas);

            this.htmlDoms = {
                code_canvas: $('#' + this.options.canvasId),
                code_btn: $('#' + this.options.submitId),
                input_text: $('#' + this.options.inputId),
                refresh_btn: $('#' + this.options.refreshId)
            };
        },
        refresh: function () {
            const canvas = this.htmlDoms.code_canvas;
            let ctx = canvas.get(0).getContext('2d');

            ctx.textBaseline = "middle";
            ctx.fillStyle = this.randomColor(180, 240);
            ctx.fillRect(0, 0, this.options.width, this.options.height);
            this.canvasContent = ctx;

            let txtArr;
            if (this.options.type === "blend") { //判断验证码类型
                txtArr = this.getAllNumber().concat(this.getAllLetter());
            } else if (this.options.type === "number") {
                txtArr = this.getAllNumber();
            } else {
                txtArr = this.getAllLetter();
            }
            this.txtArr = txtArr;

            this.generateCode();
            this.generateInterfereLine();
            this.generateWatermark();
        },
        /**生成驗證碼**/
        generateCode: function () {
            let ctx = this.canvasContent;
            this.code = '';

            for (let i = 1; i <= this.options.codeLength; i++) {
                const txt = this.txtArr[this.randomNum(0, this.txtArr.length)];
                this.code += txt;
                ctx.font = this.randomNum(this.options.height / 2, this.options.height) + 'px SimHei'; //随机生成字体大小
                ctx.fillStyle = this.randomColor(50, 160); //随机生成字体颜色        
                ctx.shadowOffsetX = this.randomNum(-3, 3);
                ctx.shadowOffsetY = this.randomNum(-3, 3);
                ctx.shadowBlur = this.randomNum(-3, 3);
                ctx.shadowColor = "rgba(0, 0, 0, 0.3)";
                const x = this.options.width / (this.options.codeLength + 1) * i;
                const y = this.options.height / 2;
                const deg = this.randomNum(-30, 30);
                /**設置旋轉角度和座標原點**/
                ctx.translate(x, y);
                ctx.rotate(deg * Math.PI / 180);
                ctx.fillText(txt, 0, 0);
                /**恢復旋轉角度和座標原點**/
                ctx.rotate(-deg * Math.PI / 180);
                ctx.translate(-x, -y);
            }
        },
        /**繪製干擾線**/
        generateInterfereLine: function () {
            let ctx = this.canvasContent;
            for (let i = 0; i < this.options.codeLength; i++) {
                ctx.strokeStyle = this.randomColor(40, 180);
                ctx.beginPath();
                ctx.moveTo(this.randomNum(0, this.options.width), this.randomNum(0, this.options.height));
                ctx.lineTo(this.randomNum(0, this.options.width), this.randomNum(0, this.options.height));
                ctx.stroke();
            }
        },
        /**繪製浮水印**/
        generateWatermark: function () {
            let ctx = this.canvasContent;
            for (let i = 0; i < this.options.width / this.options.codeLength; i++) {
                ctx.fillStyle = this.randomColor(0, 255);
                ctx.beginPath();
                ctx.arc(this.randomNum(0, this.options.width), this.randomNum(0, this.options.height), 1, 0, 2 * Math.PI);
                ctx.fill();
            }
        },
        validate: function (input) {
            const code = input.toLowerCase();
            const v_code = this.code.toLowerCase();
            console.log(v_code);
            if (code === v_code) {
                return true;
            } else {
                this.refresh();
                return false;
            }
        },
        /**生成數字組**/
        getAllNumber: function () {
            return "0,1,2,3,4,5,6,7,8,9".split(",");
        },
        /**生成文字組**/
        getAllLetter: function () {
            const letterStr = "a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z";
            return letterStr.split(",");
        },
        /**生成隨機顏色**/
        randomNum: function (min, max) {
            return Math.floor(Math.random() * (max - min) + min);
        },
        /**生成隨機顏色**/
        randomColor: function (min, max) {
            const r = this.randomNum(min, max);
            const g = this.randomNum(min, max);
            const b = this.randomNum(min, max);
            return "rgb(" + r + "," + g + "," + b + ")";
        }
    };
    /**繼承到 jQuery**/
    $.fn.codeVerify = function (options, callbacks) {
        var graphicCode = new GraphicCode(this, options);
        graphicCode._init();
        if (callbacks) {
            callbacks();
        }
    };
})(jQuery, window, document);


