var vue = new Vue({
  el: '#app',
  data:{
    accInput:'',
    pwdInput:'',
    dialogFormVisible:false,
    formData: {}
  },
  methods: {
    login() {
      const account = {
        id: 0,
        accountNum: this.accInput,
        password: this.pwdInput,
        username: "",
        others: null
      }
      axios.post("http://localhost:8080/Accounts/login", account).then((res) => {
        if (res.data.flag) {
          if (res.data.data) {
            alert("登录成功");
            sessionStorage.setItem('chatData', JSON.stringify(res.data.data));
            window.location.href = 'http://localhost:63342/chatbox/message_window/static/pages/account.html';
          }
          else
            alert(res.data.msg);
        } else {
          alert(res.data.msg);
        }
      })
    },

    handleCreate() {
      this.dialogFormVisible = true;
      this.resetForm();
    },

    cancel(){
      this.dialogFormVisible = false;
      this.resetForm();
    },

    handleAdd () {
      axios.post("http://localhost:8080/Accounts/", this.formData).then((res)=>{
        if(res.data.flag) {
          this.dialogFormVisible = false;
          this.$message.success("添加成功")
        }else{
          this.$message.error(res.data.msg)
        }
      }).catch((error)=>{
        if (error.response && error.response.status === 409) {
          const errorMsg = error.response.data?.message || error.message;
          if (errorMsg.includes("Duplicate entry") || errorMsg.includes("accountNum")) {
            this.$message.error("账号号码已存在，请使用其他账号");
          } else {
            this.$message.error("添加失败：账号号码已存在");
          }
        } else {
          this.$message.error("网络错误，请稍后重试");

        }
      });
    },

    resetForm() {
      this.formData = {};
    },

  },
})
