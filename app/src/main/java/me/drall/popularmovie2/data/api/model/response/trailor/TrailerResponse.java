package me.drall.popularmovie2.data.api.model.response.trailor;

import java.util.List;
import com.google.auto.value.AutoValue;
import com.google.gson.TypeAdapter;

import com.google.gson.annotations.SerializedName;
import com.google.gson.Gson;


@AutoValue
public abstract class TrailerResponse{

	@SerializedName("id")
	public abstract int id();

	@SerializedName("results")
	public abstract List<Trailer> results();

	public static TypeAdapter<TrailerResponse> typeAdapter(Gson gson) {
		return new AutoValue_TrailerResponse.GsonTypeAdapter(gson);
	}
}