package com.isut.customer;


public interface RecyclerViewClickListener {

    public void itemclick(String type, int position, int child_position);
    public void itemlikeclick(String type, int position, int child_position);
}
