package com.example.learnit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.snapchat.kit.sdk.SnapCreative
import com.snapchat.kit.sdk.creative.media.SnapLensLaunchData
import com.snapchat.kit.sdk.creative.models.SnapLensContent

class SubjectListAdapter(context: Context, var subjectList: List<Data>):
    RecyclerView.Adapter<SubjectListAdapter.SubjectViewHolder>() {
   val mContext = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.subjects_item, parent, false)
        return SubjectViewHolder(view)
    }
    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        val subject = subjectList[position]
        holder.image1.setImageDrawable(mContext.resources.getDrawable(subject.drawableOne))
        holder.image2.setImageDrawable(mContext.resources.getDrawable(subject.drawableTwo))
        holder.image3.setImageDrawable(mContext.resources.getDrawable(subject.drawableThree))
        holder.topics.setText(subject.title)
        holder.itemView.setOnClickListener {
            val snapLensContent:SnapLensContent = SnapLensContent(subject.LenseeId)
            val lensLaunchData = SnapLensLaunchData.Builder().addStringKeyPair("hint","Earth and Sun").build()
            snapLensContent.snapLensLaunchData = lensLaunchData
            val snapCreativeKitApi = SnapCreative.getApi(mContext)
            snapCreativeKitApi.send(snapLensContent)
            snapLensContent.captionText = "Lesson "+subject.title+" Let's learn it"
        }
    }
    override fun getItemCount(): Int {
        subjectList.isEmpty() ?: return -1
        return subjectList.size
    }
    class SubjectViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val image1 = itemView.findViewById<ImageView>(R.id.image1)
        val image2 = itemView.findViewById<ImageView>(R.id.image2)
        val image3 = itemView.findViewById<ImageView>(R.id.image3)
        val topics = itemView.findViewById<TextView>(R.id.topic_name)
    }
}