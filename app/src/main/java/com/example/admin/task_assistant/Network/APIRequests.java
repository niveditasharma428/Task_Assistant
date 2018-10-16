package com.example.admin.task_assistant.Network;

import com.example.admin.task_assistant.model.CloseData;
import com.example.admin.task_assistant.model.ContactDetails;
import com.example.admin.task_assistant.model.GroupDetails;
import com.example.admin.task_assistant.model.GroupTask;
import com.example.admin.task_assistant.model.Member;
import com.example.admin.task_assistant.model.MyGroupMembers;
import com.example.admin.task_assistant.model.MyGroups;
import com.example.admin.task_assistant.model.MyTodo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIRequests {

    @POST("userdata.php")
    @FormUrlEncoded
    Call<List<ContactDetails>> getContactDetails(@Field("mobile") String mobile);

    @POST("group_create.php")
    @FormUrlEncoded
    Call<GroupDetails> createGroup(@Field("GROUP_NAME") String GROUP_NAME,
                                   @Field("CREATED_BY") String CREATED_BY,
                                   @Field("GROUP_MEMBER") String GROUP_MEMBER);

    @POST("group_detail.php")
    @FormUrlEncoded
    Call<Member> showGroup(@Field("CREATED_BY") String CREATED_BY);

    @POST("group_delete.php")
    @FormUrlEncoded
    Call<GroupDetails> deleteGroup(@Field("CREATED_BY") String CREATED_BY,
                                   @Field("GROUP_NAME") String GROUP_NAME);

    @POST("grouptask.php")
    @FormUrlEncoded
    Call<GroupDetails> createTask(@Field("TASK_DES") String TASK_DES,
                                  @Field("TASK_PRIORITY") String TASK_PRIORITY,
                                  @Field("CREATED_BY") String CREATED_BY,
                                  @Field("TASK_GROUP") String TASK_GROUP,
                                  @Field("TASK_ASSIGN") String TASK_ASSIGN);

    @POST("grouptask_assign.php")
    @FormUrlEncoded
    Call<GroupTask> groupTaskAssignView(@Field("TASK_GROUP") String TASK_GROUP,
                                        @Field("TASK_ASSIGN") String TASK_ASSIGN);

    @POST("grouptask_reassign.php")
    @FormUrlEncoded
    Call<GroupDetails> groupTaskReAssignView(@Field("TASK_ID") String TASK_ID,
                                             @Field("TASK_COMMENT") String TASK_COMMENT);

    @POST("mytask_count.php")
    @FormUrlEncoded
    Call<MyTodo> mytask_count(@Field("mobile") String mobile);


    @POST("group_members.php")
    @FormUrlEncoded
    Call<Member> getGroupMembers(@Field("CREATED_BY") String CREATED_BY,
                                 @Field("GROUP_NAME") String GROUP_NAME

    );


    @POST("mygrouptasks.php")
    @FormUrlEncoded
    Call<GroupTask> mygrouptasks(@Field("mobile") String mobile);

    @POST("tasks_seen.php")
    @FormUrlEncoded
    Call<GroupDetails> taskseen(@Field("mobile") String mobile,
                                @Field("CREATED_BY") String CREATED_BY,
                                @Field("GROUP_NAME") String GROUP_NAME
    );

    @POST("grouptask_close.php")
    @FormUrlEncoded
    Call<GroupDetails> grouptaskclose(@Field("TASK_ID") String TASK_ID);


    @POST("closetask.php")
    @FormUrlEncoded
    Call<CloseData> closeTask(@Field("name") String name);


    @POST("closetask_delete.php")
    @FormUrlEncoded
    Call<GroupDetails> closetask_delete(@Field("TASK_ID") String TASK_ID,
                                        @Field("GROUP_NAME") String GROUP_NAME
    );

    @POST("closetask_reassign.php")
    @FormUrlEncoded
    Call<GroupDetails> closetask_reassign(@Field("TASK_ID") String TASK_ID,
                                          @Field("GROUP_NAME") String GROUP_NAME,
                                          @Field("TASK_ASSIGN") String TASK_ASSIGN
    );

    @POST("memb_for_remove.php")
    @FormUrlEncoded
    Call<Member> memb_for_remove(@Field("CREATED_BY") String CREATED_BY,
                                 @Field("GROUP_NAME") String GROUP_NAME,
                                 @Field("GROUP_ADMIN") String GROUP_ADMIN
    );


    @POST("myallgroups.php")
    @FormUrlEncoded
    Call<MyGroups> myallgroups(@Field("mobile") String mobile);

    @POST("myallgroups_members.php")
    @FormUrlEncoded
    Call<MyGroupMembers> mygroupmembers(@Field("GROUP_NAME") String GROUP_NAME,
                                        @Field("CREATED_BY") String CREATED_BY

    );


    @POST("groupmembers_remove.php")
    @FormUrlEncoded
    Call<GroupDetails> groupmembers_remove(@Field("GROUP_NAME") String GROUP_NAME,
                                           @Field("CREATED_BY") String CREATED_BY,
                                           @Field("GROUP_MEMBER") String GROUP_MEMBER);



    @POST("memb_for_adding.php")
    @FormUrlEncoded
    Call<Member> memb_for_adding(@Field("GROUP_NAME") String GROUP_NAME,
                                 @Field("CREATED_BY") String CREATED_BY
    );


    @POST("groupmembers_adding.php")
    @FormUrlEncoded
    Call<GroupDetails> groupmembers_adding(@Field("GROUP_NAME") String GROUP_NAME,
                                           @Field("CREATED_BY") String CREATED_BY,
                                           @Field("GROUP_MEMBER") String GROUP_MEMBER);






}
