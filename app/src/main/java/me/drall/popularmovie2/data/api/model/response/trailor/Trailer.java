package me.drall.popularmovie2.data.api.model.response.trailor;

import com.google.auto.value.AutoValue;
import com.google.gson.TypeAdapter;

import com.google.gson.annotations.SerializedName;
import com.google.gson.Gson;


@AutoValue
public abstract class Trailer {

	@SerializedName("site")
	public abstract String site();

	@SerializedName("size")
	public abstract int size();

	@SerializedName("iso_3166_1")
	public abstract String iso31661();

	@SerializedName("name")
	public abstract String name();

	@SerializedName("id")
	public abstract String id();

	@SerializedName("type")
	public abstract String type();

	@SerializedName("iso_639_1")
	public abstract String iso6391();

	@SerializedName("key")
	public abstract String key();

	public static TypeAdapter<Trailer> typeAdapter(Gson gson) {
		return new AutoValue_Trailer.GsonTypeAdapter(gson);
	}
}