axios.defaults.baseURL = 'http://localhost:8081'
var vue = new Vue({
    el: '#app',
    data:{
        deleteButton:false,
        friendList : [],         //当前已添加的好友
        addFriendList: [],       //搜索到的用户
        currentFriend : null,    //当前选中的聊天对象id
        messageMap: [],          //当前好友的消息列表
        myAvatar: '...',         //个人头像
        inputMsg: '',            //发送的消息
        accInput:'',             //好友搜索栏的内容
        user:{},                 //当前用户相关信息
        searchIn: {},            //保存一些输入的内容内容
        requestIn: {},           //发送好友申请后添加的内容
        activeTab: 'friendList', //左侧栏里当前展示的标签页
        searchFriend: false,     //添加好友后的搜索框是否可见
        request: false,          //添加好友窗口是否可见
        accId: null,             //暂存添加的好友id，用来发送异步请求
        accName: null,           //暂存添加的好友名称，用来发送异步请求
        requestList: [],         //好友请求列表
        settingWindow: false,    //个人设置窗口
        alterWindow: false,      //修改昵称窗口
        deleteWindow: false,     //注销确认窗口
        delfriendWindow: false,  //删除好友确认窗口
        delFriendsInfo: null,    //待删除好友的信息
    },
    created(){
        const userInfoStr = sessionStorage.getItem('chatData');
        this.user = JSON.parse(userInfoStr);
        document.title = `${this.user.username}的聊天室`;
        this.setFriendList();
    },
    beforeDestroy() {
        this.websocket.close();
    },
    computed: {
        currentMessages(){
            return this.messageMap;
        }
    },
    methods: {
        setFriendList(){
            axios.get("/Relations/"+this.user.id).then((res) => {
                this.friendList = res.data.data;
            })
            this.activeTab = 'friendList';
        },
        setRequestList(){
            axios.get("/AddRequest/"+this.user.id).then((res) => {
                this.requestList = res.data.data;
            })
            this.activeTab = 'friendRequest';
        },
        findFriends() {
            this.searchFriend = true;
        },
        setDelete(){
            this.deleteButton = !this.deleteButton;
        },
        addFriends(row){
            this.request = true;
            this.accId = row.id;
            this.accName = this.user.username;
        },
        deleteFrends() {
            this.delfriendWindow = false;
            axios.delete("/Relations/" + this.user.id + "/" + this.delFriendsInfo.friendId).then((res) => {
                if(res.data.flag) {
                    this.$message.success("删除成功")
                    this.setFriendList();
                }
            })
            if(this.delFriendsInfo.friendId === this.currentFriend){
                this.backToEmpty();
            }

        },
        cancelDelFriend(){
            this.delfriendWindow = false;
        },
        delFriend(row){
            this.delfriendWindow = true;
            this.delFriendsInfo = row;
        },
        searchUser(){
            axios.get("http://localhost:8080/Accounts/login/" + this.searchIn.accountNum).then((res) => {
                if(res.data.flag){
                    let data = res.data.data;
                    if (Array.isArray(data)) {
                        this.addFriendList = data.filter(item => item.id !== this.user.id);
                    } else {
                        this.addFriendList = (data && data.id !== this.user.id) ? [data] : [];
                    }
                }
            });
        },
        handleAdd(){
            const sendTime = this.getCurrentTime();

            const newMsg = {
                requestId : this.user.id,
                receiverId: this.accId,
                applicantName: this.accName,
                time: sendTime,
                message: this.requestIn.message
            }
            axios.post("/AddRequest/",newMsg).then((res)  => {
                if(res.data.flag){
                    this.request = false;
                    this.$message.success("已发送");
                    this.delHandleAdd();
                }else{
                    this.$message.success("你可能发送过一次了")
                }
            })
        },
        delHandleAdd(){
            this.request = false;
            this.requestIn.message = "";
        },
        getMessages(senderId, receiverId){
            axios.get("/Messages/" + senderId + "/" + receiverId).then((res) => {
                if(res.data.flag){
                    messages = res.data.data;
                    messages.forEach(item => {
                        item.isSelf = (item.receiverId !== this.user.id);
                        item.avatat = '...'
                    });
                    this.messageMap = messages;
                }
            })
        },
        messageWindow(row, column, event){
            this.currentFriend = row.friendId;
            this.getMessages(this.user.id, row.friendId);
            this.$nextTick(() => {
                this.scrollToBottom();
            })
            axios.get("http://localhost:8080/Accounts/" + row.friendId).then((res) =>{
                if(!res.data.flag){
                    this.$message.success("该好友已注销，不过消息还未删除");
                }else {
                    axios.get("/Relations/" + row.friendId + "/" + this.user.id).then((resax) => {
                        if(!resax.data.flag){
                            this.$message.error("该好友已删除了你，不过消息还未删除");
                        }
                    })
                }
            })
        },
        scrollToBottom(){
            const container = this.$refs.messageList;
            if(container){
                container.scrollTop = container.scrollHeight;
            }
        },
        sendMessage(){
            if (!this.inputMsg.trim()) return;
            if (!this.currentFriend) {
                alert('请先选择一个好友')
                return;
            }

            const newMsg = {
                senderId: this.user.id,
                receiverId: this.currentFriend,
                content: this.inputMsg,
                time: this.getCurrentTime(),
            };

            axios.post("/Messages/", newMsg).then((res) => {
                if(res.data.flag){
                    this.inputMsg = '';

                    this.$nextTick(this.scrollToBottom);
                    this.getMessages(this.user.id, this.currentFriend);
                }
            })

        },
        getCurrentTime(){
            const now = new Date();
            const year = now.getFullYear();
            const month = String(now.getMonth() + 1).padStart(2, '0');
            const day = String(now.getDate()).padStart(2, '0');
            const hours = String(now.getHours()).padStart(2, '0');
            const minutes = String(now.getMinutes()).padStart(2, '0');
            const seconds = String(now.getSeconds()).padStart(2, '0');
            return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
        },
        refreshRequests(){
            this.setRequestList();
        },
        acceptRequest(row){
            const newMsg1 = {
                selfId : row.requestId,
                friendId: this.user.id,
                username: this.user.username,
            };
            const newMsg2 = {
                selfId : this.user.id,
                friendId: row.requestId,
                username: row.applicantName,
            };
            axios.post("/Relations/", newMsg1).then((res) => {
                if(res.data.flag) {

                }
            });
            axios.post("/Relations/", newMsg2).then((res) => {
                if(res.data.flag) {
                    this.$message.success("添加成功，现在可以开始聊天了");
                    this.deletRequest(row.requestId, row.receiverId);

                }
            });

        },
        rejectRequest(row){
            this.$message.success("已拒绝");
            this.deletRequest(row.requestId, row.receiverId);
        },
        deletRequest(requestId, receiverId){
            axios.delete("/AddRequest/" + requestId + "/" + receiverId).then((res) =>{
                if(res.data.flag){
                    this.setRequestList();
                }
            });
        },
        chatFriends(){
            if(this.accInput === ""){
                this.setFriendList();
                return;
            }
            axios.get("/Relations/login/" + this.accInput).then((res) => {
                if(res.data.flag){
                    this.friendList = res.data.data;
                }
            })
        },
        setting(){
            this.settingWindow = true;
        },
        alterUsername(){
            this.alterWindow = true;
        },
        deleteUser(){
            this.deleteWindow = true;
        },
        updataUsername(){
            document.title = `${this.searchIn.username}的聊天室`;
            this.user.username = this.searchIn.username;
            axios.put("http://localhost:8080/Accounts",this.user).then((res)=>{
                if(res.data.flag) {
                    this.$message.success("修改成功")
                    sessionStorage.setItem('chatData', JSON.stringify(this.user));
                    const relate = {
                        selfId: 0,
                        friendId: this.user.id,
                        username: this.user.username
                    }
                    axios.put("/Relations",relate).then((res)=> {
                        if (res.data.flag) {
                            this.$message.success("已修改关系列表")
                        } else {
                            this.$message.error("列表修改失败,好像没人加了你")
                        }
                    });
                }else{
                    this.$message.error("修改失败")
                }
            }).catch((error)=>{
                if (error.response && error.response.status === 500) {
                    const errorMsg = error.response.data?.message || error.message;
                    if (errorMsg.includes("Duplicate entry") || errorMsg.includes("accountNum")) {
                        this.$message.error("账号号码已存在，请使用其他账号");
                    } else {
                        this.$message.error("修改失败：账号号码已存在" + errorMsg);
                    }
                } else {
                    this.$message.error("网络错误，请稍后重试");
                }
            });
        },
        affirmDelSelf(){
            axios.delete("http://localhost:8080/Accounts/" + this.user.id).then((res) => {
                if (res.data.flag) {
                    this.$message.success("已注销");
                    axios.delete("/Relations/" + this.user.id);
                } else {
                    this.$message.error("该账号已经删除了")
                }
            });
            this.deleteWindow = false;
            sessionStorage.removeItem('chatData');
            window.location.href = 'http://localhost:63342/chatbox/log_platform/index.html';
        },
        cancelDelSelf(){
            this.deleteWindow = false;
        },
        backToEmpty(){
            this.currentFriend = null;
            this.messageMap = [];
            if (this.$refs.friendTable) {
                this.$refs.friendTable.clearCurrentRow();
            }
        },
        handleCloseSearchDialog(){
            this.searchIn.accountNum = "";
            this.addFriendList = []
        }
    }
})