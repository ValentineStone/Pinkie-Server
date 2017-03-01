package com.valentine.server;

import java.util.*;

public class Arguments
{
	private Map<String, String> keys = new HashMap<>();
	private List<String> keyless = new ArrayList<>();
	
	public Arguments(String[] _args)
	{
		for (int i = 0; i < _args.length; i++)
		{
			if (_args[i].startsWith("-"))
			{
				String key   = _args[i].substring(1);
				String value = null;
				
				if ( i + 1 < _args.length && !_args[i + 1].startsWith("-"))
					value = _args[i + 1];
				
				keys.put(key, value);
			}
			else
			{
				keyless.add(_args[i]);
			}
		}
	}
	
	public String get(int _index)
	{
		if (contains(_index))
			return keyless.get(_index);
		else
			return null;
	}
	
	public String get(String _key)
	{
		return keys.get(_key);
	}
	
	public boolean contains(String _key)
	{
		return keys.containsKey(_key);
	}
	
	public boolean containsValue(String _key)
	{
		return keys.containsKey(_key);
	}
	
	public boolean contains(int _index)
	{
		return _index >= 0 && _index < keyless.size();
	}
}
